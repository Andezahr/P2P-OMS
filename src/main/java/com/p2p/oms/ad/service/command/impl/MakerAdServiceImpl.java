package com.p2p.oms.ad.service.command.impl;

import com.p2p.oms.ad.dto.request.ChangeAdStatusRequest;
import com.p2p.oms.ad.dto.request.CreateMakerAdRequest;
import com.p2p.oms.ad.dto.request.UpdateMakerAdRequest;
import com.p2p.oms.ad.entity.AdSide;
import com.p2p.oms.ad.entity.MakerAd;
import com.p2p.oms.ad.repository.MakerAdRepository;
import com.p2p.oms.ad.service.command.MakerAdService;
import com.p2p.oms.common.event.DomainEventPublisher;
import com.p2p.oms.exception.ForbiddenOperationException;
import com.p2p.oms.exception.NotFoundException;
import com.p2p.oms.user.entity.User;
import com.p2p.oms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MakerAdServiceImpl implements MakerAdService {

    private final MakerAdRepository makerAdRepository;
    private final UserRepository userRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    public MakerAd create(UUID userId, CreateMakerAdRequest request) {
        User makerUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER"));

        if (request.side() == AdSide.SELL) {
            makerUser.reserveForAd(request.amount());
        }

        MakerAd ad = MakerAd.create(
                makerUser, request.side(), request.price(),
                request.minLimit(), request.maxLimit(), request.amount()
        );

        makerAdRepository.save(ad);
        // userRepository.save(makerUser); // Не обязателен, Hibernate видит изменения в managed-объекте

        publishEvents(ad);
        return ad;
    }

    @Override
    public MakerAd update(UUID adId, UUID userId, UpdateMakerAdRequest request) {
        MakerAd ad = getOwnedAd(adId, userId);

        adjustBalanceOnUpdate(ad, request.amount());

        ad.update(request.price(), request.minLimit(), request.maxLimit(), request.amount());

        makerAdRepository.save(ad);
        publishEvents(ad);
        return ad;
    }

    @Override
    public void changeStatus(UUID adId, UUID userId, ChangeAdStatusRequest request) {
        MakerAd ad = getOwnedAd(adId, userId);
        ad.changeStatus(request.status());

        makerAdRepository.save(ad);
        publishEvents(ad);
    }

    @Override
    public void delete(UUID adId, UUID userId) {
        MakerAd ad = getOwnedAd(adId, userId);
        User makerUser = ad.getMakerUser();

        if (ad.getSide() == AdSide.SELL) {
            makerUser.releaseFromAd(ad.getAvailableAmount());
        }

        ad.delete();
        makerAdRepository.save(ad);
        publishEvents(ad);
    }

    private void adjustBalanceOnUpdate(MakerAd ad, BigDecimal newAmount) {
        if (ad.getSide() != AdSide.SELL) {
            return;
        }

        BigDecimal oldAmount = ad.getTotalAmount();
        BigDecimal delta = newAmount.subtract(oldAmount);

        User makerUser = ad.getMakerUser();
        if (delta.compareTo(BigDecimal.ZERO) > 0) {
            makerUser.reserveForAd(delta);
        } else if (delta.compareTo(BigDecimal.ZERO) < 0) {
            makerUser.releaseFromAd(delta.abs());
        }
    }

    private void publishEvents(MakerAd ad) {
        eventPublisher.publishAll(ad.pullEvents());
    }

    private MakerAd getOwnedAd(UUID adId, UUID userId) {
        MakerAd ad = makerAdRepository.findById(adId)
                .orElseThrow(NotFoundException.of("MAKER_AD"));

        if (!ad.getMakerUser().getId().equals(userId)) {
            throw new ForbiddenOperationException("Cannot manage someone else's ad");
        }
        return ad;
    }
}