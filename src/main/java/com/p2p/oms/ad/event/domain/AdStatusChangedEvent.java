package com.p2p.oms.ad.event.domain;

import com.p2p.oms.ad.entity.AdStatus;
import com.p2p.oms.common.event.DomainEvent;

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