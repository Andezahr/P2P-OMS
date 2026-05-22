package com.p2p.oms.service.query;

import com.p2p.oms.dto.response.MakerAdResponse;
import com.p2p.oms.dto.response.PageResponse;
import com.p2p.oms.query.SearchAdsCriteria;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Pageable;

@NullMarked
public interface MakerAdQueryService {

    PageResponse<MakerAdResponse> search(
            SearchAdsCriteria criteria,
            Pageable pageable
    );
}