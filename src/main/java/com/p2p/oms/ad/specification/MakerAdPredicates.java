package com.p2p.oms.ad.specification;

import com.p2p.oms.ad.entity.AdSide;
import com.p2p.oms.ad.entity.AdStatus;
import com.p2p.oms.ad.entity.MakerAd;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

@NullMarked
public final class MakerAdPredicates {

    private MakerAdPredicates() {
    }

    public static Specification<MakerAd> hasStatus(AdStatus status) {

        return (root, _, cb) ->
                cb.equal(root.get("status"), status);
    }

    public static Specification<MakerAd> hasSide(
            @Nullable AdSide side
    ) {

        return (root, _, cb) -> {

            if (side == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("side"), side);
        };
    }


    public static Specification<MakerAd> supportsAmount(
            @Nullable BigDecimal amount
    ) {

        return (root, ignored, cb) -> {

            if (amount == null) {
                return cb.conjunction();
            }

            return cb.and(

                    cb.lessThanOrEqualTo(
                            root.get("minLimit"),
                            amount
                    ),

                    cb.greaterThanOrEqualTo(
                            root.get("maxLimit"),
                            amount
                    )
            );
        };
    }


}