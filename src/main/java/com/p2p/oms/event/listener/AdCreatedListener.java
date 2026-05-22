package com.p2p.oms.event.listener;

import com.p2p.oms.event.domain.AdCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdCreatedListener {

    @EventListener
    public void handle(AdCreatedEvent event) {
        log.info("AD_CREATED: {}", event);
    }
}