package com.p2p.oms.order.service;

import com.p2p.oms.order.dto.request.CreateOrderRequest;
import com.p2p.oms.order.entity.Order;

import java.util.UUID;

public interface OrderService {

    Order create(
            CreateOrderRequest request
    );

    void markAsPaid(
            UUID orderId,
            UUID userId
    );

    void confirmPayment(
            UUID orderId,
            UUID userId
    );

    void cancel(
            UUID orderId,
            UUID userId
    );

    void openDispute(
            UUID orderId,
            UUID userId,
            String reason
    );

    void expire(
            UUID orderId
    );
}