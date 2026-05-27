package com.p2p.oms.order.event.domain;

import com.p2p.oms.common.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record OrderCancelledEvent(
        UUID eventId,
        Instant occurredAt,
        UUID orderId,
        UUID makerUserId,
        UUID takerUserId
) implements DomainEvent {

    public static OrderCancelledEvent create(UUID orderId, UUID makerUserId, UUID takerUserId) {
        return new OrderCancelledEvent(
                UUID.randomUUID(),
                Instant.now(),
                orderId,
                makerUserId,
                takerUserId
        );
    }

    @Override
    public UUID eventId() {
        return eventId;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }

    @Override
    public String eventType() {
        return "order.completed";
    }

    @Override
    public Integer eventVersion() {
        return 1;
    }
}
