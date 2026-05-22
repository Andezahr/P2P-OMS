package com.p2p.oms.repository;

import com.p2p.oms.entity.asset.CryptoAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CryptoAssetRepository extends JpaRepository<CryptoAsset, UUID> {

    Optional<CryptoAsset> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);
}