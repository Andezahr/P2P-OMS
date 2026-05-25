package com.p2p.oms.ad.event.domain;

import com.p2p.oms.common.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record AdDeletedEvent(

        UUID eventId,

        Instant occurredAt,

        UUID adId,

        UUID userId
) implements DomainEvent {
}