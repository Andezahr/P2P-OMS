package com.p2p.oms.ad.repository;

import com.p2p.oms.ad.entity.AdSide;
import com.p2p.oms.ad.entity.AdStatus;
import com.p2p.oms.ad.entity.MakerAd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface MakerAdRepository extends JpaRepository<MakerAd, UUID>,
        JpaSpecificationExecutor<MakerAd> {

    @EntityGraph(attributePaths = {
            "fiatAsset",
            "cryptoAsset",
            "paymentMethods"
    })
    Page<MakerAd> findAllByStatus(
            AdStatus status,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {
            "fiatAsset",
            "cryptoAsset",
            "paymentMethods"
    })
    Page<MakerAd> findAllBySideAndStatus(
            AdSide side,
            AdStatus status,
            Pageable pageable
    );

    @Query("""
            select ad
            from MakerAd ad
            where ad.status = :status
              and ad.side = :side
              and upper(ad.fiatAsset.code) = upper(:fiatCode)
              and upper(ad.cryptoAsset.code) = upper(:cryptoCode)
            """)
    Page<MakerAd> search(
            @Param("status") AdStatus status,
            @Param("side") AdSide side,
            @Param("fiatCode") String fiatCode,
            @Param("cryptoCode") String cryptoCode,
            Pageable pageable
    );

    List<MakerAd> findAllByUserId(UUID userId);

    @Query("""
            select ad
            from MakerAd ad
            where ad.amount >= :amount
              and ad.status = 'LISTED'
            """)
    List<MakerAd> findAvailableAds(
            @Param("amount") BigDecimal amount
    );
}