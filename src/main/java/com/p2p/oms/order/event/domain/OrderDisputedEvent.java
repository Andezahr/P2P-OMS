package com.p2p.oms.order.event.domain;

import com.p2p.oms.common.event.DomainEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderDisputedEvent (
        UUID eventId,
        Instant occurredAt,
        UUID orderId,
        UUID makerUserId,
        UUID takerUserId,
        BigDecimal amount
) implements DomainEvent {

    public static OrderDisputedEvent create(
            UUID orderId,
            UUID makerUserId,
            UUID takerUserId,
            BigDecimal amount
    ) {
        return new OrderDisputedEvent(
                UUID.randomUUID(),
                Instant.now(),
                orderId,
                makerUserId,
                takerUserId,
                amount
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
        return "order.disputed";
    }

    @Override
    public Integer eventVersion() {
        return 1;
    }
}
