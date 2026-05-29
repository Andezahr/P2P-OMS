package com.p2p.oms.common.event;

import java.util.Collection;

public interface DomainEventPublisher {

    void publish(DomainEvent event);

    void publishAll(Collection<DomainEvent> events);
}