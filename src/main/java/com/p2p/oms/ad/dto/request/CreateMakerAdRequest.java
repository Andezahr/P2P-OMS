package com.p2p.oms.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateMakerAdRequest(

        @NotNull
        UUID fiatAssetId,

        @NotNull
        UUID cryptoAssetId,

        @NotBlank
        String side,

        @NotNull
        @Positive
        BigDecimal price,

        @NotNull
        @Positive
        BigDecimal minLimit,

        @NotNull
        @Positive
        BigDecimal maxLimit,

        @NotNull
        @Positive
        BigDecimal amount,

        @NotEmpty
        List<UUID> paymentMethodIds
) {
}