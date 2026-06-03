package com.p2p.oms.order.entity;

import com.p2p.oms.ad.entity.MakerAd;
import com.p2p.oms.common.entity.BaseEntity;
import com.p2p.oms.exception.StatusSequenceViolatedException;
import com.p2p.oms.order.event.domain.*;
import com.p2p.oms.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

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
            BigDecimal amount,
            BigDecimal price
    ) {
        this.makerAd = makerAd;
        this.makerUser = makerUser;
        this.takerUser = takerUser;
        this.amount = amount;
        this.price = price;
        this.status = OrderStatus.PENDING;
        createdAt = Instant.now();
        expiresAt = Instant.now().plusSeconds(1200);
    }

    public static Order create(
            MakerAd makerAd,
            User makerUser,
            User takerUser,
            BigDecimal amount,
            BigDecimal price
    ) {
        Order order = new Order(
                makerAd,
                makerUser,
                takerUser,
                amount,
                price
        );
        order.registerEvent(
                OrderCreatedEvent.create(
                        order.getId(),
                        order.getMakerAd().getId(),
                        order.getMakerUser().getId(),
                        order.getTakerUser().getId(),
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

    public void markAsPaid() {
        if (status != OrderStatus.PENDING) {
            throw new StatusSequenceViolatedException("Only pending orders can be marked as paid");
        }

        this.status = OrderStatus.PAID;

        registerEvent(OrderPaidEvent.create(
                getId(),
                getMakerUser().getId(),
                getTakerUser().getId()
        ));
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

    public void openDispute() {

        this.status = OrderStatus.DISPUTED;

        registerEvent(
                OrderDisputedEvent.create(
                        getId(),
                        getMakerUser().getId(),
                        getTakerUser().getId(),
                        getAmount()
                )
        );
    }


    public void expire() {

        this.status = OrderStatus.CANCELLED;

        registerEvent(
                OrderExpiredEvent.create(
                        getMakerUser().getId(),
                        getTakerUser().getId(),
                        getAmount()
                )
        );
    }
}