package com.p2p.oms.ad.repository;

import com.p2p.oms.ad.entity.AdSide;
import com.p2p.oms.ad.entity.AdStatus;
import com.p2p.oms.ad.entity.MakerAd;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@NullMarked
public interface MakerAdRepository extends JpaRepository<MakerAd, UUID>,
        JpaSpecificationExecutor<MakerAd> {

    @EntityGraph(attributePaths = {
    })
    Page<MakerAd> findAllByStatus(
            AdStatus status,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {
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
            """)
    Page<MakerAd> search(
            @Param("status") AdStatus status,
            @Param("side") AdSide side,
            Pageable pageable
    );

    Page<MakerAd> findByMakerUserId(
            UUID userId,
            Pageable pageable
    );

    @Query("""
            select ad
            from MakerAd ad
            where ad.availableAmount >= :amount
              and ad.status = 'LISTED'
            """)
    List<MakerAd> findAvailableAds(
            @Param("amount") BigDecimal amount
    );
}