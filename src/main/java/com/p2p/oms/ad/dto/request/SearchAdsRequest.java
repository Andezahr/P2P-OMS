package com.p2p.oms.ad.dto.request;

import com.p2p.oms.ad.entity.AdSide;

import java.math.BigDecimal;

public record SearchAdsRequest(

        AdSide side,

        BigDecimal amount
) {
}