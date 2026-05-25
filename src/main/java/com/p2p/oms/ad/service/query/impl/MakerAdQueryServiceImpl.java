package com.p2p.oms.ad.service.query.impl;

import com.p2p.oms.ad.dto.response.MakerAdResponse;
import com.p2p.oms.ad.dto.response.PageResponse;
import com.p2p.oms.ad.entity.MakerAd;
import com.p2p.oms.ad.mapper.MakerAdMapper;
import com.p2p.oms.ad.query.SearchAdsCriteria;
import com.p2p.oms.ad.repository.MakerAdRepository;
import com.p2p.oms.ad.service.query.MakerAdQueryService;
import com.p2p.oms.ad.specification.MakerAdSpecifications;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}