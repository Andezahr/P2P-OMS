package com.p2p.oms.ad.dto.request;

import com.p2p.oms.ad.entity.AdSide;

import java.math.BigDecimal;

public record SearchAdsRequest(

        String fiat,

        String crypto,

        AdSide side,

        BigDecimal amount
) {
}