package com.p2p.oms.outbox.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent {
    @Id
    private UUID id;

    @Column(nullable = false, length = 128)
    private String eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private Instant occurredAt;

    @Column(nullable = false)
    private boolean published;

    private OutboxEvent(
            UUID id,
            String eventType,
            String payload,
            Instant occurredAt
    ) {
        this.id = id;
        this.eventType = eventType;
        this.payload = payload;
        this.occurredAt = occurredAt;
        this.published = false;
    }

    public static OutboxEvent create(
            UUID id,
            String eventType,
            String payload,
            Instant occurredAt
    ) {
        return new OutboxEvent(id, eventType, payload, occurredAt);
    }

    public void markPublished() {
        this.published = true;
    }
}
