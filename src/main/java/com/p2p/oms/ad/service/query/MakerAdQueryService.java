package com.p2p.oms.ad.service.query;

import com.p2p.oms.ad.dto.response.MakerAdResponse;
import com.p2p.oms.ad.dto.response.PageResponse;
import com.p2p.oms.ad.query.SearchAdsCriteria;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Pageable;

@NullMarked
public interface MakerAdQueryService {

    PageResponse<MakerAdResponse> search(
            SearchAdsCriteria criteria,
            Pageable pageable
    );
}