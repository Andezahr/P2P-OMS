package com.p2p.oms.ad.query;

import com.p2p.oms.ad.entity.AdSide;

import java.math.BigDecimal;

public record SearchAdsCriteria(

        String fiat,

        String crypto,

        AdSide side,

        BigDecimal amount
) {
}