package com.p2p.oms.service.command.impl;

import com.p2p.oms.dto.request.ChangeAdStatusRequest;
import com.p2p.oms.dto.request.CreateMakerAdRequest;
import com.p2p.oms.dto.request.UpdateMakerAdRequest;
import com.p2p.oms.entity.ad.AdStatus;
import com.p2p.oms.entity.ad.MakerAd;
import com.p2p.oms.entity.asset.CryptoAsset;
import com.p2p.oms.entity.asset.FiatAsset;
import com.p2p.oms.entity.payment.PaymentMethod;
import com.p2p.oms.event.domain.AdEvents;
import com.p2p.oms.event.domain.DomainEventPublisher;
import com.p2p.oms.exception.ForbiddenOperationException;
import com.p2p.oms.exception.NotFoundException;
import com.p2p.oms.mapper.MakerAdMapper;
import com.p2p.oms.repository.CryptoAssetRepository;
import com.p2p.oms.repository.FiatAssetRepository;
import com.p2p.oms.repository.MakerAdRepository;
import com.p2p.oms.repository.PaymentMethodRepository;
import com.p2p.oms.service.command.MakerAdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MakerAdServiceImpl
        implements MakerAdService {

    private final MakerAdRepository makerAdRepository;
    private final FiatAssetRepository fiatAssetRepository;
    private final CryptoAssetRepository cryptoAssetRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    private final MakerAdMapper mapper;

    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public MakerAd create(
            UUID userId,
            CreateMakerAdRequest request
    ) {

        FiatAsset fiatAsset = fiatAssetRepository.findById(
                        request.fiatAssetId()
                )
                .orElseThrow(() ->
                        new NotFoundException(
                                "Fiat asset not found"
                        )
                );

        CryptoAsset cryptoAsset = cryptoAssetRepository.findById(
                        request.cryptoAssetId()
                )
                .orElseThrow(() ->
                        new NotFoundException(
                                "Crypto asset not found"
                        )
                );

        List<PaymentMethod> paymentMethods =
                paymentMethodRepository.findAllById(
                        request.paymentMethodIds()
                );

        MakerAd ad = mapper.toEntity(
                userId,
                request,
                fiatAsset,
                cryptoAsset,
                paymentMethods
        );

        makerAdRepository.save(ad);

        eventPublisher.publish(
                AdEvents.created(ad)
        );

        return ad;
    }

    @Override
    @Transactional
    public MakerAd update(
            UUID adId,
            UUID userId,
            UpdateMakerAdRequest request
    ) {

        MakerAd ad = getOwnedAd(
                adId,
                userId
        );

        List<PaymentMethod> paymentMethods =
                paymentMethodRepository.findAllById(
                        request.paymentMethodIds()
                );

        ad.setPrice(request.price());
        ad.setMinLimit(request.minLimit());
        ad.setMaxLimit(request.maxLimit());
        ad.setAmount(request.amount());
        ad.setPaymentMethods(paymentMethods);

        makerAdRepository.save(ad);

        eventPublisher.publish(
                AdEvents.updated(ad)
        );

        return ad;
    }

    @Override
    @Transactional
    public void changeStatus(
            UUID adId,
            UUID userId,
            ChangeAdStatusRequest request
    ) {

        MakerAd ad = getOwnedAd(
                adId,
                userId
        );

        ad.setStatus(request.status());

        makerAdRepository.save(ad);

        eventPublisher.publish(
                AdEvents.statusChanged(
                        ad,
                        request.status()
                )
        );
    }

    @Override
    @Transactional
    public void delete(
            UUID adId,
            UUID userId
    ) {

        MakerAd ad = getOwnedAd(
                adId,
                userId
        );

        ad.setStatus(AdStatus.DELETED);
        ad.setDeletedAt(Instant.now());

        makerAdRepository.save(ad);

        eventPublisher.publish(
                AdEvents.deleted(ad)
        );
    }

    private MakerAd getOwnedAd(
            UUID adId,
            UUID userId
    ) {

        MakerAd ad = makerAdRepository.findById(adId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Ad not found"
                        )
                );

        if (!ad.getUserId().equals(userId)) {
            throw new ForbiddenOperationException(
                    "Cannot manage someone else's ad"
            );
        }

        return ad;
    }
}