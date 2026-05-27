package com.p2p.oms.asset.dto.response;

import java.time.Instant;
import java.util.UUID;

public record CryptoAssetResponse(

        UUID id,

        String code,

        String name,

        String blockchain,

        Integer precision,

        Boolean active,

        Instant createdAt,

        Instant updatedAt
) {
}