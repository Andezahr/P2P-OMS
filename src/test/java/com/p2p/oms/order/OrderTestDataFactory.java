package com.p2p.oms.order;

import com.p2p.oms.ad.entity.AdSide;
import com.p2p.oms.ad.entity.MakerAd;
import com.p2p.oms.ad.repository.MakerAdRepository;
import com.p2p.oms.asset.entity.CryptoAsset;
import com.p2p.oms.asset.entity.FiatAsset;
import com.p2p.oms.asset.repository.CryptoAssetRepository;
import com.p2p.oms.asset.repository.FiatAssetRepository;
import com.p2p.oms.order.entity.Order;
import com.p2p.oms.order.repository.OrderRepository;
import com.p2p.oms.payment.entity.PaymentMethod;
import com.p2p.oms.payment.repository.PaymentMethodRepository;
import com.p2p.oms.user.entity.User;
import com.p2p.oms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderTestDataFactory {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final MakerAdRepository makerAdRepository;
    private final FiatAssetRepository fiatAssetRepository;
    private final CryptoAssetRepository cryptoAssetRepository;


    @Transactional
    public Order createPendingOrder() {
        // 1. Создаём мейкера
        User maker = userRepository.save(
                User.builder().email("maker@test.com").build()
        );
        User taker = userRepository.save(
                User.builder().email("taker@test.com").build()
        );

        FiatAsset fiatAsset = fiatAssetRepository.save(new FiatAsset("RUB", "Ruble", "R", true));
        CryptoAsset cryptoAsset = cryptoAssetRepository.save(new CryptoAsset("USDT", "USD Tether", "TRC20", 3, true));
        PaymentMethod paymentMethod = paymentMethodRepository.save(PaymentMethod.builder()
                .code("SBP")
                .name("System of Fast Payments")
                .provider("CBR")
                .build());
        MakerAd makerAd = makerAdRepository.save(
                MakerAd.builder()
                        .side(AdSide.SELL)
                        .maxLimit(BigDecimal.valueOf(100000))
                        .minLimit(BigDecimal.valueOf(5000))
                        .cryptoAsset(cryptoAsset)
                        .fiatAsset(fiatAsset)
                        .makerUser(maker)
                        .paymentMethods(List.of(paymentMethod)).amount(BigDecimal.valueOf(1500))
                        .price(BigDecimal.valueOf(78))
                        .build());


        Order order = Order.create(makerAd, maker, taker, paymentMethod, new BigDecimal(500), new BigDecimal(78));

        return orderRepository.saveAndFlush(order);
    }
}