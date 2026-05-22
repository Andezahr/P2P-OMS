package com.p2p.oms.mapper;

import com.p2p.oms.dto.response.MakerAdResponse;
import com.p2p.oms.entity.ad.MakerAd;
import com.p2p.oms.entity.payment.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MakerAdMapper {

    @Mapping(target = "side", expression = "java(ad.getSide().name())")
    @Mapping(target = "status", expression = "java(ad.getStatus().name())")

    @Mapping(target = "fiatAsset", source = "fiatAsset.code")
    @Mapping(target = "cryptoAsset", source = "cryptoAsset.code")

    @Mapping(target = "paymentMethods", source = "paymentMethods")
    MakerAdResponse toResponse(MakerAd ad);

    default String map(PaymentMethod paymentMethod) {
        return paymentMethod.getCode();
    }

    List<MakerAdResponse> toResponseList(List<MakerAd> ads);
}