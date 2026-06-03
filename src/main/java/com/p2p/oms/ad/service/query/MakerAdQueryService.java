package com.p2p.oms.ad.service.query;

import com.p2p.oms.ad.dto.response.MakerAdResponse;
import com.p2p.oms.ad.dto.response.PageResponse;
import com.p2p.oms.ad.query.SearchAdsCriteria;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@NullMarked
public interface MakerAdQueryService {

    PageResponse<MakerAdResponse> search(
            SearchAdsCriteria criteria,
            Pageable pageable
    );

    PageResponse<MakerAdResponse> getUserAds(
            UUID userId,
            Pageable pageable
    );

    MakerAdResponse getAdById(UUID adId);
}