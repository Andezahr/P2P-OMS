package com.p2p.oms.common.event;

public interface DomainEventPublisher {

    void publish(DomainEvent event);
}