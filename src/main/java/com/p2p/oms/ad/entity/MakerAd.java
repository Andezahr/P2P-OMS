package com.p2p.oms.ad.entity;

import com.p2p.oms.common.entity.BaseEntity;
import com.p2p.oms.asset.entity.CryptoAsset;
import com.p2p.oms.asset.entity.FiatAsset;
import com.p2p.oms.payment.entity.PaymentMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "p2p_ads",
        indexes = {
                @Index(name = "idx_p2p_ads_user_id", columnList = "user_id"),
                @Index(name = "idx_p2p_ads_status", columnList = "status"),
                @Index(name = "idx_p2p_ads_fiat_asset_id", columnList = "fiat_asset_id"),
                @Index(name = "idx_p2p_ads_crypto_asset_id", columnList = "crypto_asset_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MakerAd extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private AdSide side;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    @Builder.Default
    private AdStatus status = AdStatus.DELISTED;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "fiat_asset_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_ad_fiat_asset")
    )
    private FiatAsset fiatAsset;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "crypto_asset_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_ad_crypto_asset")
    )
    private CryptoAsset cryptoAsset;

    @Positive
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal price;

    @Column(name = "min_limit", nullable = false, precision = 19, scale = 8)
    private BigDecimal minLimit;

    @Column(name = "max_limit", nullable = false, precision = 19, scale = 8)
    private BigDecimal maxLimit;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal amount;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "p2p_ad_payment_methods",
            joinColumns = @JoinColumn(name = "ad_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id"),
            foreignKey = @ForeignKey(name = "fk_ad_payment_method_ad"),
            inverseForeignKey = @ForeignKey(name = "fk_ad_payment_method_method")
    )
    @Builder.Default
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
}