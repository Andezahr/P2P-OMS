package com.p2p.oms.ad.service.query.impl;

import com.p2p.oms.ad.dto.response.MakerAdResponse;
import com.p2p.oms.ad.dto.response.PageResponse;
import com.p2p.oms.ad.entity.MakerAd;
import com.p2p.oms.ad.mapper.MakerAdMapper;
import com.p2p.oms.ad.query.SearchAdsCriteria;
import com.p2p.oms.ad.repository.MakerAdRepository;
import com.p2p.oms.ad.service.query.MakerAdQueryService;
import com.p2p.oms.ad.specification.MakerAdSpecifications;
import com.p2p.oms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@NullMarked
public class MakerAdQueryServiceImpl
        implements MakerAdQueryService {

    private final MakerAdRepository repository;
    private final MakerAdMapper mapper;

    @Override
    public PageResponse<MakerAdResponse> search(
            SearchAdsCriteria criteria,
            Pageable pageable
    ) {

        Page<MakerAd> page = repository.findAll(
                MakerAdSpecifications.fromCriteria(criteria),
                pageable
        );

        return new PageResponse<>(
                mapper.toResponseList(page.getContent()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public PageResponse<MakerAdResponse> getUserAds(UUID userId, Pageable pageable) {
        Page<MakerAd> page = repository.findByMakerUserId(userId, pageable);
        return mapToPageResponse(page);
    }

    @Override
    public MakerAdResponse getAdById(UUID adId) {
        MakerAd ad = repository.findById(adId)
                .orElseThrow(NotFoundException.of("MAKER_AD"));
        return mapper.toResponse(ad);
    }

    private PageResponse<MakerAdResponse> mapToPageResponse(Page<MakerAd> page) {
        return new PageResponse<>(
                mapper.toResponseList(page.getContent()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}