package com.p2p.oms.outbox.publisher;

import com.p2p.oms.outbox.entity.OutboxEvent;
import com.p2p.oms.outbox.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@NullMarked
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxEventRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void publish() {
        List<OutboxEvent> events = repository.findTop100ByPublishedFalseOrderByOccurredAtAsc();
        for (OutboxEvent event : events) {
            kafkaTemplate.send(
                    event.getEventType(),
                    event.getId().toString(),
                    event.getPayload()
            );

            log.info(
                    """
                    Publishing event:
                    eventId={}
                    eventType={}
                    payload={}
                    """,
                    event.getId(),
                    event.getEventType(),
                    event.getPayload()
            );
            event.markPublished();
        }
    }
}
