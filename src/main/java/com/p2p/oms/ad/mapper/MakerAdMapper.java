package com.p2p.oms.ad.mapper;

import com.p2p.oms.ad.dto.request.CreateMakerAdRequest;
import com.p2p.oms.ad.dto.response.MakerAdResponse;
import com.p2p.oms.ad.entity.MakerAd;
import com.p2p.oms.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MakerAdMapper {

    @Mapping(target = "makerUserId", source = "makerUser.id")
    MakerAdResponse toResponse(MakerAd ad);

    List<MakerAdResponse> toResponseList(List<MakerAd> ads);

    default MakerAd toEntity(User makerUser, CreateMakerAdRequest request) {
        return MakerAd.create(
                makerUser,
                request.side(),
                request.price(),
                request.minLimit(),
                request.maxLimit(),
                request.amount()
        );
    }
}