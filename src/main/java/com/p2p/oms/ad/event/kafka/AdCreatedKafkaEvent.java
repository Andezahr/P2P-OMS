package com.p2p.oms.ad.event.kafka;

import java.math.BigDecimal;
import java.util.UUID;

public record AdCreatedKafkaEvent(
        UUID adId,
        UUID userId,
        String fiat,
        String crypto,
        BigDecimal price
) {
}