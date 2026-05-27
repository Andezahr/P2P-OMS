package com.p2p.oms.config;

import com.p2p.oms.asset.entity.CryptoAsset;
import com.p2p.oms.asset.entity.FiatAsset;
import com.p2p.oms.payment.entity.PaymentMethod;
import com.p2p.oms.asset.repository.CryptoAssetRepository;
import com.p2p.oms.asset.repository.FiatAssetRepository;
import com.p2p.oms.payment.repository.PaymentMethodRepository;
import com.p2p.oms.user.entity.User;
import com.p2p.oms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final FiatAssetRepository fiatAssetRepository;
    private final CryptoAssetRepository cryptoAssetRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;

    @NullMarked
    @Override
    public void run(String... args) {
        seedFiatAssets();
        seedCryptoAssets();
        seedPaymentMethods();
        seedUser();
    }

    private void seedFiatAssets() {

        if (fiatAssetRepository.count() > 0) {
            return;
        }

        fiatAssetRepository.save(
                FiatAsset.builder()
                        .code("RUB")
                        .name("Russian Ruble")
                        .symbol("₽")
                        .build()
        );
    }

    private void seedCryptoAssets() {

        if (cryptoAssetRepository.count() > 0) {
            return;
        }

        cryptoAssetRepository.save(
                CryptoAsset.builder()
                        .code("USDT")
                        .name("Tether USD")
                        .blockchain("TRC20")
                        .precision(2)
                        .build()
        );
    }

    private void seedPaymentMethods() {

        if (paymentMethodRepository.count() > 0) {
            return;
        }

        paymentMethodRepository.save(
                PaymentMethod.builder()
                        .code("SBP")
                        .name("System of Fast Payments")
                        .provider("CBR")
                        .build()
        );
    }

    private void seedUser() {
        if (userRepository.count() > 0) {
            return;
        }
        User us = new User();
        userRepository.save(us);
    }
}