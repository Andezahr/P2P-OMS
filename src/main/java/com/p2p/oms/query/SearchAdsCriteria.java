package com.p2p.oms.query;

import com.p2p.oms.entity.ad.AdSide;

import java.math.BigDecimal;

public record SearchAdsCriteria(

        String fiat,

        String crypto,

        AdSide side,

        BigDecimal amount
) {
}