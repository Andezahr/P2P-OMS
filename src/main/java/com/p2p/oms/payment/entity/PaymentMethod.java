package com.p2p.oms.payment.entity;

import com.p2p.oms.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "payment_methods",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_payment_method_code", columnNames = "code")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod extends BaseEntity {

    @Column(nullable = false, length = 32)
    private String code;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(length = 128)
    private String provider;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}