package com.p2p.oms.asset.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCryptoAssetRequest(

        @NotBlank
        @Size(max = 16)
        String code,

        @NotBlank
        @Size(max = 64)
        String name,

        @Size(max = 128)
        String blockchain,

        Integer precision,

        Boolean active
) {
}