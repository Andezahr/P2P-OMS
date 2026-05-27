package com.p2p.oms.order.entity;

import com.p2p.oms.ad.entity.MakerAd;
import com.p2p.oms.common.entity.BaseEntity;
import com.p2p.oms.exception.StatusSequenceViolatedException;
import com.p2p.oms.order.event.domain.OrderCancelledEvent;
import com.p2p.oms.order.event.domain.OrderCompletedEvent;
import com.p2p.oms.order.event.domain.OrderCreatedEvent;
import com.p2p.oms.payment.entity.PaymentMethod;
import com.p2p.oms.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(name = "idx_orders_status", columnList = "status"),
                @Index(name = "idx_orders_maker_user", columnList = "maker_user_id"),
                @Index(name = "idx_orders_taker_user", columnList = "taker_user_id"),
                @Index(name = "idx_orders_ad", columnList = "maker_ad_id"),
                @Index(name = "idx_orders_expires_at", columnList = "expires_at")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "maker_ad_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_ad")
    )
    private MakerAd makerAd;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "maker_user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_maker_user")
    )
    private User makerUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "taker_user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_taker_user")
    )
    private User takerUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "payment_method_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_payment_method")
    )
    private PaymentMethod paymentMethod;

    @Positive
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal amount;

    @Positive
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private OrderStatus status;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    private Order(
            MakerAd makerAd,
            User makerUser,
            User takerUser,
            PaymentMethod paymentMethod,
            BigDecimal amount,
            BigDecimal price,
            Instant expiresAt
    ) {
        this.makerAd = makerAd;
        this.makerUser = makerUser;
        this.takerUser = takerUser;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.price = price;
        this.expiresAt = expiresAt;
        this.status = OrderStatus.CREATED;
    }

    public static Order create(
            MakerAd makerAd,
            User makerUser,
            User takerUser,
            PaymentMethod paymentMethod,
            BigDecimal amount,
            BigDecimal price,
            Instant expiresAt
    ) {
        Order order = new Order(
                makerAd,
                makerUser,
                takerUser,
                paymentMethod,
                amount,
                price,
                expiresAt
        );
        order.registerEvent(
                OrderCreatedEvent.create(
                        order.getId(),
                        order.getMakerAd().getId(),
                        order.getMakerUser().getId(),
                        order.getTakerUser().getId(),
                        order.getPaymentMethod().getId(),
                        order.getAmount(),
                        order.getPrice(),
                        order.getExpiresAt()
                )
        );
        return order;
    }

    public void cancel() {

        if (status == OrderStatus.COMPLETED) {
            throw new StatusSequenceViolatedException("Completed order cannot be cancelled");
        }

        this.status = OrderStatus.CANCELLED;

        registerEvent(
                OrderCancelledEvent.create(
                        getId(),
                        getMakerUser().getId(),
                        getTakerUser().getId()
                )
        );
    }

    public void complete() {

        if (status != OrderStatus.PAID) {
            throw new StatusSequenceViolatedException("Only paid orders can be completed");
        }

        this.status = OrderStatus.COMPLETED;

        registerEvent(
                OrderCompletedEvent.create(
                        getId(),
                        getMakerUser().getId(),
                        getTakerUser().getId()
                )
        );
    }
}