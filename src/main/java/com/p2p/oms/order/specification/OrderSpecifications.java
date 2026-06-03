package com.p2p.oms.order.specification;

import com.p2p.oms.order.entity.Order;
import com.p2p.oms.order.query.OrderSearchCriteria;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

@NullMarked
public final class OrderSpecifications {

    private OrderSpecifications() {}

    public static Specification<Order> myOrders(UUID userId, OrderSearchCriteria criteria) {
        return (root, query, cb) -> {
            var isMaker = cb.equal(root.get("makerUser").get("id"), userId);
            var isTaker = cb.equal(root.get("takerUser").get("id"), userId);

            var predicate = cb.or(isMaker, isTaker);

            if (criteria.status() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), criteria.status()));
            }

            if (criteria.role() != null) {
                predicate = cb.and(predicate, switch (criteria.role()) {
                    case MAKER -> isMaker;
                    case TAKER -> isTaker;
                });
            }

            return predicate;
        };
    }
}