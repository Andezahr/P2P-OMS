package com.p2p.oms.order.event.domain;

import com.p2p.oms.common.event.DomainEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID eventId,
        Instant occurredAt,
        UUID orderId,
        UUID makerAdId,
        UUID makerUserId,
        UUID takerUserId,
        BigDecimal amount,
        BigDecimal price,
        Instant expiresAt
) implements DomainEvent {

    @Override
    public String eventType() {
        return "order.created";
    }

    @Override
    public Integer eventVersion() {
        return 1;
    }

    public static OrderCreatedEvent create(
            UUID orderId,
            UUID makerAdId,
            UUID makerUserId,
            UUID takerUserId,
            BigDecimal amount,
            BigDecimal price,
            Instant expiresAt
    ) {

        return new OrderCreatedEvent(
                UUID.randomUUID(),
                Instant.now(),
                orderId,
                makerAdId,
                makerUserId,
                takerUserId,
                amount,
                price,
                expiresAt
        );
    }
}