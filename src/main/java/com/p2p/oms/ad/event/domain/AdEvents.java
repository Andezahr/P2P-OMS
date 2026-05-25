package com.p2p.oms.ad.event.domain;

import com.p2p.oms.ad.entity.AdStatus;
import com.p2p.oms.ad.entity.MakerAd;

import java.time.Instant;
import java.util.UUID;

public final class AdEvents {

    private AdEvents() {
    }

    public static AdCreatedEvent created(
            MakerAd ad
    ) {

        return new AdCreatedEvent(
                UUID.randomUUID(),
                Instant.now(),
                ad.getId(),
                ad.getUserId(),
                ad.getSide().name(),
                ad.getFiatAsset().getCode(),
                ad.getCryptoAsset().getCode(),
                ad.getPrice()
        );
    }

    public static AdUpdatedEvent updated(
            MakerAd ad
    ) {

        return new AdUpdatedEvent(
                UUID.randomUUID(),
                Instant.now(),
                ad.getId(),
                ad.getUserId()
        );
    }

    public static AdDeletedEvent deleted(
            MakerAd ad
    ) {

        return new AdDeletedEvent(
                UUID.randomUUID(),
                Instant.now(),
                ad.getId(),
                ad.getUserId()
        );
    }

    public static AdStatusChangedEvent statusChanged(
            MakerAd ad,
            AdStatus newStatus
    ) {

        return new AdStatusChangedEvent(
                UUID.randomUUID(),
                Instant.now(),
                ad.getId(),
                ad.getUserId(),
                newStatus
        );
    }
}