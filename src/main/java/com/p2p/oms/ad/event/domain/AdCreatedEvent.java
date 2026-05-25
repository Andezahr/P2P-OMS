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
        String fiat,
        String crypto,
        BigDecimal price
) implements DomainEvent {
}