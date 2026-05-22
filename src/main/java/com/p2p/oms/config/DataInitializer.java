package com.p2p.oms.config;

import com.p2p.oms.entity.asset.CryptoAsset;
import com.p2p.oms.entity.asset.FiatAsset;
import com.p2p.oms.entity.payment.PaymentMethod;
import com.p2p.oms.repository.CryptoAssetRepository;
import com.p2p.oms.repository.FiatAssetRepository;
import com.p2p.oms.repository.PaymentMethodRepository;
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

    @NullMarked
    @Override
    public void run(String... args) {
        seedFiatAssets();
        seedCryptoAssets();
        seedPaymentMethods();
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
}