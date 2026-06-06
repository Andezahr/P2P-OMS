package com.p2p.oms.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import com.p2p.oms.telegram.service.TelegramBindingService;

@Slf4j
@Component
public class NotificationBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final String botToken;
    private final TelegramBindingService bindingService;

    public NotificationBot(@Value("${telegram.bot.token}") String botToken,
                           TelegramBindingService bindingService) {
        this.botToken = botToken;
        this.bindingService = bindingService;
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public String getBotToken() { return botToken; }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() { return this; }

    public void sendMessage(String chatId, String text) {
        try {
            telegramClient.execute(SendMessage.builder().chatId(chatId).text(text).build());
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chatId={}", chatId, e);
        }
    }

    @Override
    public void consume(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        String chatId = update.getMessage().getChatId().toString();
        String text = update.getMessage().getText().trim();

        if (text.startsWith("/link ")) {
            handleLinkCommand(chatId, text.substring(6).trim());
        } else if ("/start".equals(text)) {
            sendMessage(chatId, "Привет! Чтобы привязать аккаунт, отправь /link <код>, полученный в личном кабинете.");
        }
    }

    private void handleLinkCommand(String chatId, String token) {
        try {
            bindingService.bindByToken(chatId, token);
            sendMessage(chatId, "✅ Аккаунт успешно привязан! Теперь вы будете получать уведомления.");
            log.info("Telegram bound: chatId={}", chatId);
        } catch (IllegalArgumentException e) {
            sendMessage(chatId, "❌ Неверный код.");
        } catch (IllegalStateException e) {
            sendMessage(chatId, "❌ " + e.getMessage());
        } catch (Exception e) {
            log.error("Error binding chatId={}", chatId, e);
            sendMessage(chatId, "❌ Произошла ошибка. Попробуйте позже.");
        }
    }
}