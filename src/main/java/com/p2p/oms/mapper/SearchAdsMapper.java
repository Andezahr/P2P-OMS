package com.p2p.oms.mapper;

import com.p2p.oms.dto.request.SearchAdsRequest;
import com.p2p.oms.query.SearchAdsCriteria;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface SearchAdsMapper {

    SearchAdsCriteria toCriteria(
            SearchAdsRequest request
    );
}