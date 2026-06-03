package com.p2p.oms.order.service.query;

import com.p2p.oms.exception.ForbiddenOperationException;
import com.p2p.oms.exception.NotFoundException;
import com.p2p.oms.order.dto.OrderMapper;
import com.p2p.oms.order.dto.response.OrderResponse;
import com.p2p.oms.order.dto.response.PageResponse;
import com.p2p.oms.order.entity.Order;
import com.p2p.oms.order.query.OrderSearchCriteria;
import com.p2p.oms.order.repository.OrderRepository;
import com.p2p.oms.order.specification.OrderSpecifications;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@NullMarked
@Transactional(readOnly = true)
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse getById(UUID orderId, UUID userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("ORDER"));

        // Проверка: пользователь может видеть только свои ордера
        if (!isParticipant(order, userId)) {
            throw new ForbiddenOperationException("You are not a participant of this order");
        }

        return orderMapper.toResponse(order);
    }

    @Override
    public PageResponse<OrderResponse> getMyOrders(
            UUID userId,
            OrderSearchCriteria criteria,
            Pageable pageable
    ) {
        Page<Order> page = orderRepository.findAll(
                OrderSpecifications.myOrders(userId, criteria),
                pageable
        );
        return mapToPageResponse(page);
    }

    private boolean isParticipant(Order order, UUID userId) {
        return order.getMakerUser().getId().equals(userId)
                || order.getTakerUser().getId().equals(userId);
    }

    private PageResponse<OrderResponse> mapToPageResponse(Page<Order> page) {
        return new PageResponse<>(
                orderMapper.toResponseList(page.getContent()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}