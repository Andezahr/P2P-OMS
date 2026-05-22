package com.p2p.oms.repository;

import com.p2p.oms.entity.payment.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {

    Optional<PaymentMethod> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);
}