package com.p2p.oms.order.service;

import com.p2p.oms.ad.entity.MakerAd;
import com.p2p.oms.ad.repository.MakerAdRepository;
import com.p2p.oms.common.event.DomainEventPublisher;
import com.p2p.oms.exception.NotFoundException;
import com.p2p.oms.order.dto.OrderMapper;
import com.p2p.oms.order.dto.request.CreateOrderRequest;
import com.p2p.oms.order.dto.response.OrderResponse;
import com.p2p.oms.order.entity.Order;
import com.p2p.oms.order.entity.OrderStatus;
import com.p2p.oms.order.repository.OrderRepository;
import com.p2p.oms.user.entity.User;
import com.p2p.oms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


/**
 * One-side order service where maker always sells crypto
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DomainEventPublisher eventPublisher;
    private final MakerAdRepository makerAdRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse create(CreateOrderRequest request) {

        MakerAd makerAd = makerAdRepository.findById(request.makerAdId()).orElseThrow(NotFoundException.of("MAKER_AD"));
        User makerUser = makerAd.getMakerUser();
        User takerUser = userRepository.findById(request.takerUserId()).orElseThrow(NotFoundException.of("TAKER_USER"));

        Order order = Order.create(
                makerAd,
                makerAd.getMakerUser(),
                takerUser,
                request.amount(),
                makerAd.getPrice()
        );

        makerAd.reserve(request.amount()); // Freeze liquidity in ad
        makerUser.reserveForOrder(request.amount()); // Freeze maker's money
        persist(order);
        makerAdRepository.save(makerAd);
        userRepository.save(makerUser);
        return orderMapper.toResponse(order);
    }

    @Override
    public void markAsPaid(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(NotFoundException.of("ORDER"));

        order.markAsPaid();
        persist(order);
    }

    @Override
    public void complete(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(NotFoundException.of("ORDER"));
        User makerUser = order.getMakerUser();
        User takerUser = order.getTakerUser();

        order.complete();
        makerUser.completeOrder(order.getAmount()); // Releasing money in user
        takerUser.deposit(order.getAmount()); // Releasing money to buyer
        order.getMakerAd().complete(order.getAmount()); // Releasing liquidity in ad

        persist(order);
        userRepository.save(makerUser);
        userRepository.save(takerUser);
        makerAdRepository.save(order.getMakerAd());
    }

    @Override
    public void cancel(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(NotFoundException.of("ORDER"));
        User makerUser = order.getMakerUser();
        MakerAd makerAd = order.getMakerAd();

        order.cancel();
        makerUser.releaseFromOrder(order.getAmount());
        makerAd.release(order.getAmount());

        persist(order);
        userRepository.save(makerUser);
        makerAdRepository.save(makerAd);
    }

    @Override
    public void openDispute(UUID orderId, UUID disputeInitiatorId, String reason) {
        Order order = orderRepository.findById(orderId).orElseThrow(NotFoundException.of("ORDER"));
        // Redo money freeze if unfrozen
        if (order.getStatus() == OrderStatus.CANCELLED) {
            User makerUser = order.getMakerUser();
            makerUser.reserveForOrder(order.getAmount());
            userRepository.save(makerUser);
        }

        order.openDispute();
        persist(order);
    }

    @Override
    public void expire(UUID orderId) {
        Order order = getOrder(orderId);
        User makerUser = order.getMakerUser();
        order.expire();
        makerUser.releaseFromOrder(order.getAmount());
        order.getMakerAd().release(order.getAmount());
        persist(order);
        userRepository.save(makerUser);
        makerAdRepository.save(order.getMakerAd());
    }

    private void persist(Order order) {
        orderRepository.saveAndFlush(order);
        eventPublisher.publishAll(order.pullEvents());
    }

    private Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(NotFoundException.of("ORDER"));
    }
}
