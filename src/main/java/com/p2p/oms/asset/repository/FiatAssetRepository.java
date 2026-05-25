package com.p2p.oms.asset.repository;

import com.p2p.oms.asset.entity.FiatAsset;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@NullMarked
public interface FiatAssetRepository extends JpaRepository<FiatAsset, UUID> {

    Optional<FiatAsset> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);
}