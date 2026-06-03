package com.p2p.oms.order;

import com.p2p.oms.ad.entity.AdSide;
import com.p2p.oms.ad.entity.MakerAd;
import com.p2p.oms.ad.repository.MakerAdRepository;
import com.p2p.oms.order.entity.Order;
import com.p2p.oms.order.repository.OrderRepository;
import com.p2p.oms.user.entity.User;
import com.p2p.oms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class OrderTestDataFactory {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final MakerAdRepository makerAdRepository;

    @Transactional
    public Order createPaidOrder() {
        User maker = User.create("maker@test.com");
        User taker = userRepository.save(User.create("taker@test.com"));

        BigDecimal orderAmount = new BigDecimal("500");

        // 1. Пополняем баланс мейкера
        maker.deposit(BigDecimal.valueOf(5000));

        // 2. Сохраняем мейкера, чтобы он получил ID и стал managed
        userRepository.saveAndFlush(maker);

        // 3. Создаем объявление
        MakerAd makerAd = MakerAd.create(
                maker,
                AdSide.SELL,
                BigDecimal.valueOf(100), // price
                BigDecimal.valueOf(100),    // minLimit
                BigDecimal.valueOf(1000),   // maxLimit
                BigDecimal.valueOf(2000)    // totalAmount
        );
        makerAdRepository.saveAndFlush(makerAd);

        // 4. ЭМУЛИРУЕМ СОЗДАНИЕ ОБЪЯВЛЕНИЯ: Резервируем средства мейкера под это объявление
        maker.reserveForAd(orderAmount);

        // 5. ЭМУЛИРУЕМ СОЗДАНИЕ ОРДЕРА: Переводим средства из резерва объявления в резерв ордера
        maker.reserveForOrder(orderAmount);

        // 6. Резервируем ликвидность в самом объявлении
        makerAd.reserve(orderAmount);

        // Сохраняем изменения балансов и ликвидности
        userRepository.save(maker);
        makerAdRepository.save(makerAd);

        // 7. Создаем и оплачиваем ордер
        Order order = Order.create(makerAd, maker, taker, orderAmount, new BigDecimal("78"));
        order.markAsPaid();

        return orderRepository.save(order);
    }
}