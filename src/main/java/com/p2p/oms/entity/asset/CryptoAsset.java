package com.p2p.oms.entity.asset;

import com.p2p.oms.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CryptoAsset extends BaseEntity {

    @Column(nullable = false, length = 16)
    private String code;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(length = 128)
    private String blockchain;

    @Column(nullable = false)
    @Builder.Default
    private Integer precision = 8;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}