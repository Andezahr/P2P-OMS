package com.p2p.oms.common.event;

import com.p2p.oms.outbox.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class OutboxDomainEventPublisher implements DomainEventPublisher{

    private final OutboxService outboxService;

    @Override
    public void publish(DomainEvent event) {
        outboxService.save(event);
    }

    @Override
    public void publishAll(Collection<DomainEvent> events) {
        events.forEach(outboxService::save);
    }
}
