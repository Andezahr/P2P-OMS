package com.p2p.oms.service.query.impl;

import com.p2p.oms.dto.response.MakerAdResponse;
import com.p2p.oms.dto.response.PageResponse;
import com.p2p.oms.entity.ad.MakerAd;
import com.p2p.oms.mapper.MakerAdMapper;
import com.p2p.oms.query.SearchAdsCriteria;
import com.p2p.oms.repository.MakerAdRepository;
import com.p2p.oms.service.query.MakerAdQueryService;
import com.p2p.oms.specification.MakerAdSpecifications;
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