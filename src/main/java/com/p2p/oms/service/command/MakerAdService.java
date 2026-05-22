package com.p2p.oms.service.command;

import com.p2p.oms.dto.request.CreateMakerAdRequest;
import com.p2p.oms.entity.ad.MakerAd;

import java.util.UUID;

public interface MakerAdService {

    MakerAd create(UUID userId, CreateMakerAdRequest request);

    void delete(UUID adId, UUID userId);
}