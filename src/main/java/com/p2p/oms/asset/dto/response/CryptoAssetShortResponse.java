package com.p2p.oms.asset.dto.response;

import java.util.UUID;

public record CryptoAssetShortResponse(

        UUID id,

        String code,

        String name
) {
}