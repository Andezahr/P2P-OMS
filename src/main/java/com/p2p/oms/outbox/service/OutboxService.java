package com.p2p.oms.outbox.service;

import com.p2p.oms.common.event.DomainEvent;
import com.p2p.oms.outbox.entity.OutboxEvent;
import com.p2p.oms.outbox.repository.OutboxEventRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public void save(DomainEvent event) {
        String payload = objectMapper.writeValueAsString(event);
        OutboxEvent outboxEvent = OutboxEvent.create(
                event.eventId(),
                event.eventType(),
                payload,
                event.occurredAt()
        );
        outboxEventRepository.save(outboxEvent);
    }
}
