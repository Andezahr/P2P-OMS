package com.p2p.oms.event.domain;

public interface DomainEventPublisher {

    void publish(DomainEvent event);
}