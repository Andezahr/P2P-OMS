package com.p2p.oms.specification;

import com.p2p.oms.entity.ad.AdStatus;
import com.p2p.oms.entity.ad.MakerAd;
import com.p2p.oms.query.SearchAdsCriteria;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.domain.Specification;

@NullMarked
public final class MakerAdSpecifications {

    private MakerAdSpecifications() {
    }

    public static Specification<MakerAd> fromCriteria(
            SearchAdsCriteria criteria
    ) {

        return Specification.allOf(
                MakerAdPredicates.hasStatus(AdStatus.LISTED),
                MakerAdPredicates.hasSide(criteria.side()),
                MakerAdPredicates.hasFiat(criteria.fiat()),
                MakerAdPredicates.hasCrypto(criteria.crypto()),
                MakerAdPredicates.supportsAmount(criteria.amount())
        );
    }
}