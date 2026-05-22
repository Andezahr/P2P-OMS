package com.p2p.oms.controller;

import com.p2p.oms.dto.request.ChangeAdStatusRequest;
import com.p2p.oms.dto.request.CreateMakerAdRequest;
import com.p2p.oms.dto.request.SearchAdsRequest;
import com.p2p.oms.dto.request.UpdateMakerAdRequest;
import com.p2p.oms.dto.response.MakerAdResponse;
import com.p2p.oms.dto.response.PageResponse;
import com.p2p.oms.entity.ad.AdSide;
import com.p2p.oms.entity.ad.MakerAd;
import com.p2p.oms.mapper.MakerAdMapper;
import com.p2p.oms.mapper.SearchAdsMapper;
import com.p2p.oms.service.command.MakerAdService;
import com.p2p.oms.service.query.MakerAdQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ads")
public class MakerAdController {

    private final MakerAdQueryService queryService;
    private final SearchAdsMapper searchAdsMapper;
    private final MakerAdService makerAdService;
    private final MakerAdMapper mapper;

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

    @PostMapping
    public MakerAdResponse create(
            @RequestHeader("X-User-Id")
            UUID userId,

            @Valid
            @RequestBody
            CreateMakerAdRequest request
    ) {

        MakerAd ad = makerAdService.create(
                userId,
                request
        );

        return mapper.toResponse(ad);
    }

    @PutMapping("/{adId}")
    public MakerAdResponse update(
            @PathVariable
            UUID adId,

            @RequestHeader("X-User-Id")
            UUID userId,

            @Valid
            @RequestBody
            UpdateMakerAdRequest request
    ) {

        MakerAd ad = makerAdService.update(
                adId,
                userId,
                request
        );

        return mapper.toResponse(ad);
    }

    @PatchMapping("/{adId}/status")
    public void changeStatus(
            @PathVariable
            UUID adId,

            @RequestHeader("X-User-Id")
            UUID userId,

            @Valid
            @RequestBody
            ChangeAdStatusRequest request
    ) {

        makerAdService.changeStatus(
                adId,
                userId,
                request
        );
    }

    @DeleteMapping("/{adId}")
    public void delete(
            @PathVariable
            UUID adId,

            @RequestHeader("X-User-Id")
            UUID userId
    ) {

        makerAdService.delete(
                adId,
                userId
        );
    }
}