package com.p2p.oms.ad.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record MakerAdResponse(
        UUID id,
        UUID userId,

        String side,
        String status,

        BigDecimal price,
        BigDecimal minLimit,
        BigDecimal maxLimit,
        BigDecimal amount,

        Instant createdAt
) {
}