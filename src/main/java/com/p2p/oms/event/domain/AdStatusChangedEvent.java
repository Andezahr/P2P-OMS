package com.p2p.oms.event.domain;

import com.p2p.oms.entity.ad.AdStatus;

import java.time.Instant;
import java.util.UUID;

public record AdStatusChangedEvent(

        UUID eventId,

        Instant occurredAt,

        UUID adId,

        UUID userId,

        AdStatus newStatus

) implements DomainEvent {
}