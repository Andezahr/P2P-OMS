package com.p2p.oms.order.event.domain;

import com.p2p.oms.common.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record OrderPaidEvent (
        UUID eventId,
        Instant occurredAt,
        UUID orderId,
        UUID makerUserId,
        UUID takerUserId
) implements DomainEvent {

    public static OrderPaidEvent create(UUID orderId, UUID makerUserId, UUID takerUserId) {
        return new OrderPaidEvent(
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
        return "order.paid";
    }

    @Override
    public Integer eventVersion() {
        return 1;
    }
}
