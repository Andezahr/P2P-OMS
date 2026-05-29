package com.p2p.oms.order.event.domain;

import com.p2p.oms.common.event.DomainEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderExpiredEvent(
        UUID eventId,
        Instant occurredAt,
        UUID makerUserId,
        UUID takerUserId,
        BigDecimal amount
) implements DomainEvent {

    public static OrderExpiredEvent create(
            UUID makerUserId,
            UUID takerUserId,
            BigDecimal amount
    ) {
        return new OrderExpiredEvent(
                UUID.randomUUID(),
                Instant.now(),
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
        return "order.expired";
    }

    @Override
    public Integer eventVersion() {
        return 1;
    }
}
