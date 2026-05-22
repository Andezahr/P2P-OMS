package com.p2p.oms.service.command;

import com.p2p.oms.dto.request.ChangeAdStatusRequest;
import com.p2p.oms.dto.request.CreateMakerAdRequest;
import com.p2p.oms.dto.request.UpdateMakerAdRequest;
import com.p2p.oms.entity.ad.MakerAd;

import java.util.UUID;

public interface MakerAdService {

    MakerAd create(
            UUID userId,
            CreateMakerAdRequest request
    );

    MakerAd update(
            UUID adId,
            UUID userId,
            UpdateMakerAdRequest request
    );

    void changeStatus(
            UUID adId,
            UUID userId,
            ChangeAdStatusRequest request
    );

    void delete(
            UUID adId,
            UUID userId
    );
}