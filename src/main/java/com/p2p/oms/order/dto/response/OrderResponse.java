package com.p2p.oms.order.dto.response;

import com.p2p.oms.order.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(

        UUID id,

        UUID makerAdId,

        UUID makerUserId,

        UUID takerUserId,

        BigDecimal amount,

        BigDecimal price,

        OrderStatus status,

        Instant expiresAt,

        Instant createdAt,

        Instant updatedAt

) {
}