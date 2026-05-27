package com.p2p.oms.asset.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateCryptoAssetRequest(

        @Size(max = 64)
        String name,

        @Size(max = 128)
        String blockchain,

        Integer precision,

        Boolean active
) {
}