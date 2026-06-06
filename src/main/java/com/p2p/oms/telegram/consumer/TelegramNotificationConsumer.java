package com.p2p.oms.telegram.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.p2p.oms.telegram.NotificationBot;
import com.p2p.oms.telegram.service.UserTelegramMappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramNotificationConsumer {

    private final NotificationBot telegramBot;
    private final UserTelegramMappingService mappingService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = {"order.created", "order.paid", "order.completed", "order.cancelled"},
            groupId = "telegram-notifications"
    )
    public void consumeNotificationEvent(
            @Payload String payload,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_KEY) String key
    ) {
        try {
            log.info("Received Kafka event: topic={}, key={}", topic, key);

            JsonNode jsonNode = objectMapper.readTree(payload);

            // Собираем всех участников ордера (множество убирает дубликаты)
            Set<UUID> participants = extractParticipants(jsonNode);

            if (participants.isEmpty()) {
                log.warn("No participants found in payload for topic={}", topic);
                return;
            }

            String messageText = generateMessage(jsonNode, topic);

            // Отправляем уведомление каждому участнику
            for (UUID userId : participants) {
                sendToParticipant(userId, messageText);
            }

        } catch (Exception e) {
            log.error("Error processing Kafka message from topic={}", topic, e);
        }
    }

    /**
     * Извлекает всех участников ордера (maker и taker).
     * Использует Set, чтобы избежать дубликатов, если maker == taker.
     */
    private Set<UUID> extractParticipants(JsonNode jsonNode) {
        Set<UUID> participants = new HashSet<>();

        addUserId(jsonNode, "makerUserId", participants);
        addUserId(jsonNode, "takerUserId", participants);

        return participants;
    }

    private void addUserId(JsonNode jsonNode, String fieldName, Set<UUID> participants) {
        String userIdStr = jsonNode.path(fieldName).asText(null);
        if (userIdStr != null && !userIdStr.isBlank()) {
            try {
                participants.add(UUID.fromString(userIdStr));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid UUID format in field {}: {}", fieldName, userIdStr);
            }
        }
    }

    private void sendToParticipant(UUID userId, String messageText) {
        String chatId = mappingService.getChatIdByUserId(userId);
        if (chatId == null) {
            log.debug("User {} has no Telegram binding, skipping", userId);
            return;
        }

        try {
            telegramBot.sendMessage(chatId, messageText);
            log.info("Notification sent to user {} (chatId={})", userId, chatId);
        } catch (Exception e) {
            log.error("Failed to send message to user {} (chatId={})", userId, chatId, e);
        }
    }

    private String generateMessage(JsonNode jsonNode, String topic) {
        String orderId = jsonNode.path("orderId").asText("unknown");
        String shortId = orderId.length() >= 8 ? orderId.substring(0, 8) : orderId;

        return switch (topic) {
            case "order.created" -> String.format("📦 Новый ордер #%s создан", shortId);
            case "order.paid" -> String.format("✅ Ордер #%s оплачен", shortId);
            case "order.completed" -> String.format("🎉 Ордер #%s завершён", shortId);
            case "order.cancelled" -> String.format("❌ Ордер #%s отменён", shortId);
            default -> "Новое событие по ордеру";
        };
    }
}