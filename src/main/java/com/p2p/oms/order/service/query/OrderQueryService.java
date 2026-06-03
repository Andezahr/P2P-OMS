package com.p2p.oms.order.service.query;

import com.p2p.oms.order.dto.response.OrderResponse;
import com.p2p.oms.order.dto.response.PageResponse;
import com.p2p.oms.order.query.OrderSearchCriteria;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderQueryService {
    OrderResponse getById(UUID orderId, UUID userId);
    PageResponse<OrderResponse> getMyOrders(UUID userId, OrderSearchCriteria criteria, Pageable pageable);
}