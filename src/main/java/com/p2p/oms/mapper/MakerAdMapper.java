package com.p2p.oms.mapper;

import com.p2p.oms.dto.request.CreateMakerAdRequest;
import com.p2p.oms.dto.response.MakerAdResponse;
import com.p2p.oms.entity.ad.MakerAd;
import com.p2p.oms.entity.asset.CryptoAsset;
import com.p2p.oms.entity.asset.FiatAsset;
import com.p2p.oms.entity.payment.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface MakerAdMapper {

    @Mapping(target = "fiatAsset", source = "fiatAsset.code")
    @Mapping(target = "cryptoAsset", source = "cryptoAsset.code")
    MakerAdResponse toResponse(MakerAd ad);

    List<MakerAdResponse> toResponseList(
            List<MakerAd> ads
    );

    @Mapping(target = "status", constant = "LISTED")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "side", source = "request.side")
    @Mapping(target = "price", source = "request.price")
    @Mapping(target = "minLimit", source = "request.minLimit")
    @Mapping(target = "maxLimit", source = "request.maxLimit")
    @Mapping(target = "amount", source = "request.amount")
    @Mapping(target = "fiatAsset", source = "fiatAsset")
    @Mapping(target = "cryptoAsset", source = "cryptoAsset")
    @Mapping(target = "paymentMethods", source = "paymentMethods")
    @Mapping(target = "deletedAt", ignore = true)
    MakerAd toEntity(
            UUID userId,
            CreateMakerAdRequest request,
            FiatAsset fiatAsset,
            CryptoAsset cryptoAsset,
            List<PaymentMethod> paymentMethods
    );

    default String map(
            PaymentMethod paymentMethod
    ) {

        return paymentMethod.getCode();
    }
}