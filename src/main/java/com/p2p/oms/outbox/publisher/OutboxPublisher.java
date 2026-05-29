package com.p2p.oms.outbox.publisher;

import com.p2p.oms.outbox.entity.OutboxEvent;
import com.p2p.oms.outbox.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxEventRepository repository;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publish() {
        List<OutboxEvent> events = repository.findTop100ByPublishedFalseOrderByOccurredAtAsc();
        for (OutboxEvent event : events) {
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
