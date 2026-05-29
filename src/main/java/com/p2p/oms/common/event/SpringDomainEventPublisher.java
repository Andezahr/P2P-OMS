package com.p2p.oms.common.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(DomainEvent event) {

        publisher.publishEvent(event);
    }

    @Override
    public void publishAll(Collection<DomainEvent> events) {

        events.forEach(publisher::publishEvent);
    }
}