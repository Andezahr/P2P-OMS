package com.p2p.oms.payment.repository;

import com.p2p.oms.payment.entity.PaymentMethod;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@NullMarked
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {

    Optional<PaymentMethod> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);
}