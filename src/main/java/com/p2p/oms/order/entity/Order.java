package com.p2p.oms.order.entity;

import com.p2p.oms.ad.entity.MakerAd;
import com.p2p.oms.common.entity.BaseEntity;
import com.p2p.oms.payment.entity.PaymentMethod;
import com.p2p.oms.user.User;
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
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}