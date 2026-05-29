package com.p2p.oms.order.service;

import com.p2p.oms.ad.entity.MakerAd;
import com.p2p.oms.ad.repository.MakerAdRepository;
import com.p2p.oms.common.event.DomainEventPublisher;
import com.p2p.oms.exception.NotFoundException;
import com.p2p.oms.order.dto.OrderMapper;
import com.p2p.oms.order.dto.request.CreateOrderRequest;
import com.p2p.oms.order.dto.response.OrderResponse;
import com.p2p.oms.order.entity.Order;
import com.p2p.oms.order.repository.OrderRepository;
import com.p2p.oms.payment.entity.PaymentMethod;
import com.p2p.oms.payment.repository.PaymentMethodRepository;
import com.p2p.oms.user.entity.User;
import com.p2p.oms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DomainEventPublisher eventPublisher;
    private final MakerAdRepository makerAdRepository;
    private final UserRepository userRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse create(CreateOrderRequest request) {

        MakerAd makerAd = makerAdRepository.findById(request.makerAdId()).orElseThrow(NotFoundException.of("MAKER_AD"));
        User takerUser = userRepository.findById(request.takerUserId()).orElseThrow(NotFoundException.of("TAKER_USER"));
        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.paymentMethodId()).orElseThrow(NotFoundException.of("PAYMENT_METHOD"));

        Order order = Order.create(
                makerAd,
                makerAd.getMakerUser(),
                takerUser,
                paymentMethod,
                request.amount(),
                makerAd.getPrice()
        );
        
        persist(order);

        /*
        Добавить логику заморозки денег в ордере
        Вероятно надо реализовать через перевод на эскроу
        */

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
        order.complete();
        persist(order);
        /*
        реализовать логику перевода денег между мейкером и тейкером
        */
    }

    @Override
    public void cancel(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(NotFoundException.of("ORDER"));
        order.cancel();
        persist(order);
        /*
        Реализовать логику освобождения денег от заморозки
        */

    }

    @Override
    public void openDispute(UUID orderId, UUID disputeInitiatorId, String reason) {
        Order order = orderRepository.findById(orderId).orElseThrow(NotFoundException.of("ORDER"));
        order.openDispute();
        persist(order);
    }

    @Override
    public void expire(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(NotFoundException.of("ORDER"));
        order.expire();
        persist(order);
    }

    private void persist(Order order) {
        orderRepository.save(order);
        eventPublisher.publishAll(order.pullEvents());
    }
}
