package com.p2p.oms.event.domain;

import java.time.Instant;
import java.util.UUID;

public record AdUpdatedEvent(

        UUID eventId,

        Instant occurredAt,

        UUID adId,

        UUID userId
) implements DomainEvent {
}