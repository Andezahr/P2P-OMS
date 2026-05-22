package com.p2p.oms.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MakerAdResponse(
        UUID id,
        UUID userId,

        String side,
        String status,

        String fiatAsset,
        String cryptoAsset,

        BigDecimal price,
        BigDecimal minLimit,
        BigDecimal maxLimit,
        BigDecimal amount,

        List<String> paymentMethods,

        Instant createdAt
) {
}