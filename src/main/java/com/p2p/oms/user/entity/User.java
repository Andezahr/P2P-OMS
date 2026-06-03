package com.p2p.oms.user.entity;

import com.p2p.oms.ad.entity.MakerAd;
import com.p2p.oms.common.entity.BaseEntity;
import com.p2p.oms.exception.CriticalLogicException;
import com.p2p.oms.exception.InsufficientBalanceException;
import com.p2p.oms.order.entity.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_users_email", columnList = "email", unique = true)
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 128)
    private String email;

    @OneToMany(
            mappedBy = "makerUser",
            fetch = FetchType.LAZY
    )

    private List<MakerAd> makerAds = new ArrayList<>();

    @OneToMany(
            mappedBy = "makerUser",
            fetch = FetchType.LAZY
    )

    private List<Order> makerOrders = new ArrayList<>();

    @OneToMany(
            mappedBy = "takerUser",
            fetch = FetchType.LAZY
    )

    private List<Order> takerOrders = new ArrayList<>();

    @PositiveOrZero
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal balance = BigDecimal.ZERO;

    @PositiveOrZero
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal adReservedBalance = BigDecimal.ZERO;

    @PositiveOrZero
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal orderReservedBalance = BigDecimal.ZERO;

    private User(String email) {
        this.email = email;
    }

    public static User create(String email) {
        return new User(email);
    }

    public BigDecimal availableBalance() {
        return balance
                .subtract(adReservedBalance)
                .subtract(orderReservedBalance);
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        ensureAvailable(amount);
        balance = balance.subtract(amount);
    }

    /**
    Ad balance logic
     */

    public void reserveForAd(BigDecimal amount) {

        ensureAvailable(amount);

        adReservedBalance = adReservedBalance.add(amount);
    }

    public void releaseFromAd(BigDecimal amount) {
        if (adReservedBalance.compareTo(amount) < 0) {
            throw new CriticalLogicException(amount.toString());
        }

        adReservedBalance = adReservedBalance.subtract(amount);
    }

    /**
    Order balance logic
     */

    public void reserveForOrder(BigDecimal amount) {

        if (adReservedBalance.compareTo(amount) < 0) {
            throw new CriticalLogicException(amount.toString());
        }

        adReservedBalance = adReservedBalance.subtract(amount);
        orderReservedBalance = orderReservedBalance.add(amount);
    }

    public void releaseFromOrder(BigDecimal amount) {

        if (orderReservedBalance.compareTo(amount) < 0) {
            throw new CriticalLogicException(amount.toString());
        }

        orderReservedBalance = orderReservedBalance.subtract(amount);
        adReservedBalance = adReservedBalance.add(amount);
    }

    public void completeOrder(BigDecimal amount) {

        if (orderReservedBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException(amount.toString());
        }

        orderReservedBalance = orderReservedBalance.subtract(amount);
        balance = balance.subtract(amount);
    }

    private void ensureAvailable(BigDecimal amount) {
        if (availableBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(amount.toString());
        }
    }

}