package com.p2p.oms.outbox.repository;

import com.p2p.oms.outbox.entity.OutboxEvent;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

@NullMarked
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findTop100ByPublishedFalseOrderByOccurredAtAsc();
}
