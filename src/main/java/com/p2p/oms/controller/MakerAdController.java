package com.p2p.oms.controller;

import com.p2p.oms.dto.request.SearchAdsRequest;
import com.p2p.oms.dto.response.MakerAdResponse;
import com.p2p.oms.dto.response.PageResponse;
import com.p2p.oms.entity.ad.AdSide;
import com.p2p.oms.mapper.SearchAdsMapper;
import com.p2p.oms.service.query.MakerAdQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ads")
public class MakerAdController {

    private final MakerAdQueryService queryService;
    private final SearchAdsMapper searchAdsMapper;

    @GetMapping
    public PageResponse<MakerAdResponse> search(

            @RequestParam(required = false)
            String fiat,

            @RequestParam(required = false)
            String crypto,

            @RequestParam(required = false)
            AdSide side,

            @RequestParam(required = false)
            BigDecimal amount,

            Pageable pageable
    ) {

        SearchAdsRequest request =
                new SearchAdsRequest(
                        fiat,
                        crypto,
                        side,
                        amount
                );

        return queryService.search(
                searchAdsMapper.toCriteria(request),
                pageable
        );
    }


}