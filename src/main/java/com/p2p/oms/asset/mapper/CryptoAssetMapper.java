package com.p2p.oms.asset.mapper;

import com.p2p.oms.asset.dto.request.CreateCryptoAssetRequest;
import com.p2p.oms.asset.dto.response.CryptoAssetResponse;
import com.p2p.oms.asset.entity.CryptoAsset;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CryptoAssetMapper {

    CryptoAsset toEntity(
            CreateCryptoAssetRequest request
    );

    CryptoAssetResponse toResponse(
            CryptoAsset asset
    );

    List<CryptoAssetResponse> toResponseList(
            List<CryptoAsset> assets
    );
}