package com.p2p.oms.asset.repository;

import com.p2p.oms.asset.entity.CryptoAsset;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@NullMarked
public interface CryptoAssetRepository extends JpaRepository<CryptoAsset, UUID> {

    Optional<CryptoAsset> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);
}