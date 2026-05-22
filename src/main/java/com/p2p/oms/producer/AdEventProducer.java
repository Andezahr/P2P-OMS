package com.p2p.oms.producer;

import com.p2p.oms.event.kafka.AdCreatedKafkaEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishAdCreated(AdCreatedKafkaEvent event) {
        kafkaTemplate.send("ads.created", event.adId().toString(), event);
    }
}