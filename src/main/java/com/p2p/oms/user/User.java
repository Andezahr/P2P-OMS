package com.p2p.oms.user;

import com.p2p.oms.ad.entity.MakerAd;
import com.p2p.oms.common.entity.BaseEntity;
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
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 128)
    private String email;

    @PositiveOrZero
    @Column(nullable = false, precision = 19, scale = 8)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @OneToMany(
            mappedBy = "makerUser",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<MakerAd> makerAds = new ArrayList<>();

    @OneToMany(
            mappedBy = "makerUser",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Order> makerOrders = new ArrayList<>();

    @OneToMany(
            mappedBy = "takerUser",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Order> takerOrders = new ArrayList<>();
}