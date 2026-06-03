package com.p2p.oms.order.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderRequest(

        @NotNull
        UUID makerAdId,

        @Positive
        BigDecimal amount
) {
}