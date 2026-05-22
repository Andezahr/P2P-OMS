package com.p2p.oms.repository;

import com.p2p.oms.entity.asset.FiatAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FiatAssetRepository extends JpaRepository<FiatAsset, UUID> {

    Optional<FiatAsset> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);
}