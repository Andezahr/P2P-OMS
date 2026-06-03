package com.p2p.oms.ad.dto.request;

import com.p2p.oms.ad.entity.AdSide;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public record CreateMakerAdRequest(

        @NotNull
        AdSide side,

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
        BigDecimal amount
) {
}