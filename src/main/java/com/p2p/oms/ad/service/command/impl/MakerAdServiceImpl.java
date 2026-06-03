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

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MakerAdServiceImpl implements MakerAdService {

    private final MakerAdRepository makerAdRepository;
    private final UserRepository userRepository;

    private final DomainEventPublisher eventPublisher;

    @Override
    public MakerAd create(
            UUID userId,
            CreateMakerAdRequest request
    ) {

        User makerUser = userRepository
                .findById(userId)
                .orElseThrow(NotFoundException.of("USER"));

        if (request.side() == AdSide.SELL) {
            makerUser.reserveForAd(request.amount());
        }

        MakerAd ad = MakerAd.create(
                makerUser,
                request.side(),
                request.price(),
                request.minLimit(),
                request.maxLimit(),
                request.amount()
        );

        persist(ad);

        userRepository.saveAndFlush(makerUser);

        return ad;
    }

    @Override
    public MakerAd update(
            UUID adId,
            UUID userId,
            UpdateMakerAdRequest request
    ) {

        MakerAd ad = getOwnedAd(
                adId,
                userId
        );

        User makerUser = getUser(request, ad);

        ad.update(
                request.price(),
                request.minLimit(),
                request.maxLimit(),
                request.amount()
        );

        persist(ad);

        userRepository.saveAndFlush(makerUser);

        return ad;
    }

    private static User getUser(UpdateMakerAdRequest request, MakerAd ad) {
        User makerUser = ad.getMakerUser();

        if (ad.getSide() == AdSide.SELL) {

            if (request.amount().compareTo(ad.getTotalAmount()) > 0) {

                makerUser.reserveForAd(
                        request.amount().subtract(ad.getTotalAmount())
                );

            } else if (request.amount().compareTo(ad.getTotalAmount()) < 0) {

                makerUser.releaseFromAd(
                        ad.getTotalAmount().subtract(request.amount())
                );
            }
        }
        return makerUser;
    }

    @Override
    public void changeStatus(
            UUID adId,
            UUID userId,
            ChangeAdStatusRequest request
    ) {

        MakerAd ad = getOwnedAd(
                adId,
                userId
        );

        ad.changeStatus(request.status());

        persist(ad);
    }

    @Override
    public void delete(
            UUID adId,
            UUID userId
    ) {

        MakerAd ad = getOwnedAd(
                adId,
                userId
        );

        User makerUser = ad.getMakerUser();

        if (ad.getSide() == AdSide.SELL) {
            makerUser.releaseFromAd(ad.getAvailableAmount());
        }

        ad.delete();

        persist(ad);

        userRepository.saveAndFlush(makerUser);
    }

    private void persist(MakerAd ad) {
        makerAdRepository.saveAndFlush(ad);
        eventPublisher.publishAll(ad.pullEvents());
    }

    private MakerAd getOwnedAd(
            UUID adId,
            UUID userId
    ) {

        MakerAd ad = makerAdRepository.findById(adId)
                .orElseThrow(NotFoundException.of("MAKER_AD"));

        if (!ad.getMakerUser().getId().equals(userId)) {
            throw new ForbiddenOperationException(
                    "Cannot manage someone else's ad"
            );
        }

        return ad;
    }
}