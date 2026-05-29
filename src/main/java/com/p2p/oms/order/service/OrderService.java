package com.p2p.oms.order.service;

import com.p2p.oms.order.dto.request.CreateOrderRequest;
import com.p2p.oms.order.dto.response.OrderResponse;
import java.util.UUID;

public interface OrderService {

    OrderResponse create(
            CreateOrderRequest request
    );

    void markAsPaid(
            UUID orderId
    );

    void complete(
            UUID orderId
    );

    void cancel(
            UUID orderId
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