package com.p2p.oms.order.query;

import com.p2p.oms.order.entity.OrderStatus;
import com.p2p.oms.order.entity.OrderRole;

public record OrderSearchCriteria(
        OrderStatus status,
        OrderRole role
) {}