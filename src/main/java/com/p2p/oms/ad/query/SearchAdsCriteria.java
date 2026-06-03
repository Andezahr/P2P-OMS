package com.p2p.oms.ad.query;

import com.p2p.oms.ad.entity.AdSide;

import java.math.BigDecimal;

public record SearchAdsCriteria(

        AdSide side,

        BigDecimal amount
) {
}