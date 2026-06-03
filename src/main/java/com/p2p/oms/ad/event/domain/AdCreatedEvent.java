package com.p2p.oms.ad.event.domain;

import com.p2p.oms.common.event.DomainEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AdCreatedEvent(
        UUID eventId,
        Instant occurredAt,
        UUID adId,
        UUID userId,
        String side,
        BigDecimal price
) implements DomainEvent {
    @Override
    public String eventType() {
        return "ad.created";
    }

    @Override
    public Integer eventVersion() {
        return 1;
    }
}