package com.p2p.oms.ad.entity;

import com.p2p.oms.common.entity.BaseEntity;
import com.p2p.oms.exception.InsufficientLiquidityException;
import com.p2p.oms.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "p2p_ads",
        indexes = {
                @Index(name = "idx_p2p_ads_user_id", columnList = "user_id"),
                @Index(name = "idx_p2p_ads_status", columnList = "status"),
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MakerAd extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_ad_maker_user")
    )
    private User makerUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private AdSide side;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private AdStatus status;

    @Positive
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal price;

    @Positive
    @Column(name = "min_limit", nullable = false, precision = 19, scale = 8)
    private BigDecimal minLimit;

    @Positive
    @Column(name = "max_limit", nullable = false, precision = 19, scale = 8)
    private BigDecimal maxLimit;

    @Positive
    @Column(name = "total_amount", nullable = false, precision = 19, scale = 8)
    private BigDecimal totalAmount;

    @Positive
    @Column(name = "available_amount", nullable = false, precision = 19, scale = 8)
    private BigDecimal availableAmount;

    @Column(name = "reserved_amount", nullable = false, precision = 19, scale = 8)
    private BigDecimal reservedAmount;

    private MakerAd(User makerUser, AdSide side, BigDecimal price, BigDecimal minLimit,
                    BigDecimal maxLimit, BigDecimal totalAmount
    ) {
        this.makerUser = makerUser;
        this.side = side;
        this.status = AdStatus.DELISTED;
        this.price = price;
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
        this.totalAmount = totalAmount;
        this.availableAmount = totalAmount;
        this.reservedAmount = BigDecimal.ZERO;
    }

    public static MakerAd create(User makerUser, AdSide side, BigDecimal price, BigDecimal minLimit,
                                 BigDecimal maxLimit, BigDecimal totalAmount
    ) {
        return new MakerAd(makerUser, side, price, minLimit,
                maxLimit, totalAmount
        );
    }

    public void reserve(BigDecimal amount) {

        if (availableAmount.compareTo(amount) < 0) {
            throw new InsufficientLiquidityException(amount.toString());
        }

        availableAmount = availableAmount.subtract(amount);
        reservedAmount = reservedAmount.add(amount);
    }

    public void release(BigDecimal amount) {

        if (reservedAmount.compareTo(amount) < 0) {
            throw new InsufficientLiquidityException(amount.toString());
        }

        reservedAmount = reservedAmount.subtract(amount);
        availableAmount = availableAmount.add(amount);
    }

    public void complete(BigDecimal amount) {

        if (reservedAmount.compareTo(amount) < 0) {
            throw new InsufficientLiquidityException(amount.toString());
        }

        reservedAmount = reservedAmount.subtract(amount);
        totalAmount = totalAmount.subtract(amount);

        if (totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            status = AdStatus.DELISTED;
        }
    }

    public void changeStatus(AdStatus status) {
        switch (status) {
            case LISTED -> {
                if (availableAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new InsufficientLiquidityException("Cannot activate ad without liquidity");
                }
                this.status = AdStatus.LISTED;
            }
            case DELISTED ->
                    this.status = AdStatus.DELISTED;
            case DELETED ->
                    this.status = AdStatus.DELETED;
            default ->
                    throw new IllegalStateException("Unsupported status transition");
        }
    }

    public void update(
            BigDecimal price,
            BigDecimal minLimit,
            BigDecimal maxLimit,
            BigDecimal totalAmount
    ) {
        this.price = price;
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
        this.totalAmount = totalAmount;

        this.availableAmount =
                totalAmount.subtract(reservedAmount);

        if (this.availableAmount.compareTo(BigDecimal.ZERO) == 0) {
            this.status = AdStatus.DELISTED;
        }
    }

    public void delete() {
        this.status = AdStatus.DELETED;
    }
}