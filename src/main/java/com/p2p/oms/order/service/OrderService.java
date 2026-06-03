package com.p2p.oms.order.service;

import com.p2p.oms.order.dto.request.CreateOrderRequest;
import com.p2p.oms.order.dto.response.OrderResponse;
import java.util.UUID;

public interface OrderService {

    OrderResponse create(
            UUID takerUserId,
            CreateOrderRequest request
    );

    void markAsPaid(
            UUID orderId,
            UUID userId
    );

    void complete(
            UUID orderId,
            UUID userId
    );

    void cancel(
            UUID orderId,
            UUID userId
    );

    void openDispute(
            UUID orderId,
            UUID disputeInitiatorId,
            String reason
    );

    void expire(
            UUID orderId
    );
}