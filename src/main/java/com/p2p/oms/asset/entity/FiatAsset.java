package com.p2p.oms.asset.entity;

import com.p2p.oms.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "fiat_assets",
        uniqueConstraints = {
                @UniqueConstraint(name = "fiat_asset_code", columnNames = "code")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FiatAsset extends BaseEntity {

    @Column(nullable = false, length = 16)
    private String code;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(length = 8)
    private String symbol;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}