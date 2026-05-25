package com.p2p.oms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record UpdateMakerAdRequest(

        @Positive
        BigDecimal price,

        @Positive
        BigDecimal minLimit,

        @Positive
        BigDecimal maxLimit,

        @Positive
        BigDecimal amount,

        @NotEmpty
        List<UUID> paymentMethodIds
) {
}