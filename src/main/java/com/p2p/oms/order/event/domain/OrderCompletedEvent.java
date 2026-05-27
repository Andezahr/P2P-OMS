package com.p2p.oms.order.event.domain;

import com.p2p.oms.common.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record OrderCompletedEvent(
        UUID eventId,
        Instant occurredAt,
        UUID orderId,
        UUID makerUserId,
        UUID takerUserId
) implements DomainEvent {

    public static OrderCompletedEvent create(UUID orderId, UUID makerUserId, UUID takerUserId) {
        return new OrderCompletedEvent(
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
