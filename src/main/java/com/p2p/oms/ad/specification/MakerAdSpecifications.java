package com.p2p.oms.ad.specification;

import com.p2p.oms.ad.entity.AdStatus;
import com.p2p.oms.ad.entity.MakerAd;
import com.p2p.oms.ad.query.SearchAdsCriteria;
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
                MakerAdPredicates.supportsAmount(criteria.amount())
        );
    }
}