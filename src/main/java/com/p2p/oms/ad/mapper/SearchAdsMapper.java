package com.p2p.oms.ad.mapper;

import com.p2p.oms.ad.dto.request.SearchAdsRequest;
import com.p2p.oms.ad.query.SearchAdsCriteria;
import com.p2p.oms.config.CentralMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface SearchAdsMapper {

    SearchAdsCriteria toCriteria(
            SearchAdsRequest request
    );
}