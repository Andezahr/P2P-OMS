package com.p2p.oms.ad.event.domain;

import com.p2p.oms.common.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record AdUpdatedEvent(

        UUID eventId,

        Instant occurredAt,

        UUID adId,

        UUID userId
) implements DomainEvent {
    @Override
    public String eventType() {
        return "ad.updated";
    }

    @Override
    public Integer eventVersion() {
        return 1;
    }
}