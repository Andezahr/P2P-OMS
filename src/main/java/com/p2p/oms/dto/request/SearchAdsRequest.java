package com.p2p.oms.dto.request;

import com.p2p.oms.entity.ad.AdSide;

import java.math.BigDecimal;

public record SearchAdsRequest(

        String fiat,

        String crypto,

        AdSide side,

        BigDecimal amount
) {
}