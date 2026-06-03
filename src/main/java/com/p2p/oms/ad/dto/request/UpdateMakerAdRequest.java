package com.p2p.oms.ad.dto.request;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateMakerAdRequest(

        @Positive
        BigDecimal price,

        @Positive
        BigDecimal minLimit,

        @Positive
        BigDecimal maxLimit,

        @Positive
        BigDecimal amount
) {
}