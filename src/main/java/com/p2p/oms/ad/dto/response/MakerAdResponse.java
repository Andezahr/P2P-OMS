package com.p2p.oms.ad.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record MakerAdResponse(
        UUID id,
        UUID makerUserId,

        String side,
        String status,

        BigDecimal price,
        BigDecimal minLimit,
        BigDecimal maxLimit,
        BigDecimal totalAmount,

        Instant createdAt
) {
}