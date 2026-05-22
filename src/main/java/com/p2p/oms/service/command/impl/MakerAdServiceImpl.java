package com.p2p.oms.service.command.impl;

import com.p2p.oms.dto.request.CreateMakerAdRequest;
import com.p2p.oms.entity.ad.*;
import com.p2p.oms.entity.asset.*;
import com.p2p.oms.entity.payment.PaymentMethod;
import com.p2p.oms.event.domain.AdCreatedEvent;
import com.p2p.oms.exception.ForbiddenOperationException;
import com.p2p.oms.exception.NotFoundException;
import com.p2p.oms.repository.*;
import com.p2p.oms.service.command.MakerAdService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MakerAdServiceImpl implements MakerAdService {

    private final MakerAdRepository makerAdRepository;
    private final FiatAssetRepository fiatAssetRepository;
    private final CryptoAssetRepository cryptoAssetRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public MakerAd create(UUID userId, CreateMakerAdRequest request) {

        FiatAsset fiatAsset = fiatAssetRepository.findById(request.fiatAssetId())
                .orElseThrow(() -> new NotFoundException("Fiat asset not found"));

        CryptoAsset cryptoAsset = cryptoAssetRepository.findById(request.cryptoAssetId())
                .orElseThrow(() -> new NotFoundException("Crypto asset not found"));

        List<PaymentMethod> paymentMethods = paymentMethodRepository
                .findAllById(request.paymentMethodIds());

        MakerAd ad = MakerAd.builder()
                .userId(userId)
                .side(AdSide.valueOf(request.side()))
                .status(AdStatus.LISTED)
                .fiatAsset(fiatAsset)
                .cryptoAsset(cryptoAsset)
                .price(request.price())
                .minLimit(request.minLimit())
                .maxLimit(request.maxLimit())
                .amount(request.amount())
                .paymentMethods(paymentMethods)
                .build();

        makerAdRepository.save(ad);

        eventPublisher.publishEvent(
                new AdCreatedEvent(
                        UUID.randomUUID(),
                        Instant.now(),
                        ad.getId(),
                        ad.getUserId(),
                        ad.getSide().name(),
                        ad.getFiatAsset().getCode(),
                        ad.getCryptoAsset().getCode(),
                        ad.getPrice()
                )
        );

        return ad;
    }

    @Override
    @Transactional
    public void delete(UUID adId, UUID userId) {

        MakerAd ad = makerAdRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException("Ad not found"));

        if (!ad.getUserId().equals(userId)) {
            throw new ForbiddenOperationException("Cannot delete someone else's ad");
        }

        ad.setStatus(AdStatus.DELETED);

        makerAdRepository.save(ad);
    }
}