package com.p2p.oms.order.controller;

import com.p2p.oms.order.dto.request.CreateOrderRequest;
import com.p2p.oms.order.dto.request.DisputeRequest;
import com.p2p.oms.order.dto.response.OrderResponse;
import com.p2p.oms.order.dto.response.PageResponse;
import com.p2p.oms.order.query.OrderSearchCriteria;
import com.p2p.oms.order.service.OrderService;
import com.p2p.oms.order.service.query.OrderQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@NullMarked
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderQueryService orderQueryService;

    // --- Query Operations ---

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable UUID orderId,
            @RequestAttribute("userId") UUID userId
    ) {
        return ResponseEntity.ok(orderQueryService.getById(orderId, userId));
    }

    @GetMapping("/my")
    public ResponseEntity<PageResponse<OrderResponse>> getMyOrders(
            @RequestAttribute("userId") UUID userId,
            @ModelAttribute OrderSearchCriteria criteria,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(orderQueryService.getMyOrders(userId, criteria, pageable));
    }

    // --- Command Operations ---

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestAttribute("userId") UUID takerUserId,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        return ResponseEntity.ok(orderService.create(takerUserId, request));
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<Void> markAsPaid(
            @PathVariable UUID orderId,
            @RequestAttribute("userId") UUID userId
    ) {
        orderService.markAsPaid(orderId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/complete")
    public ResponseEntity<Void> complete(
            @PathVariable UUID orderId,
            @RequestAttribute("userId") UUID userId
    ) {
        orderService.complete(orderId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancel(
            @PathVariable UUID orderId,
            @RequestAttribute("userId") UUID userId
    ) {
        orderService.cancel(orderId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/dispute")
    public ResponseEntity<Void> openDispute(
            @PathVariable UUID orderId,
            @RequestAttribute("userId") UUID initiatorId,
            @Valid @RequestBody DisputeRequest request
    ) {
        orderService.openDispute(orderId, initiatorId, request.reason());
        return ResponseEntity.ok().build();
    }

}