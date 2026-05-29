package com.p2p.oms.order;

import com.p2p.oms.order.entity.Order;
import com.p2p.oms.order.entity.OrderStatus;
import com.p2p.oms.order.repository.OrderRepository;
import com.p2p.oms.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OrderConcurrencyTest {

    @Autowired private OrderService orderService;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderTestDataFactory testDataFactory;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = testDataFactory.createPendingOrder();
    }

    @Test
    void should_allow_only_one_complete() throws InterruptedException {
        int threads = 2;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(threads);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);

        try (var executor = Executors.newFixedThreadPool(threads)) {
            for (int i = 0; i < threads; i++) {
                executor.submit(() -> {
                    try {
                        startLatch.await();
                        orderService.markAsPaid(testOrder.getId());
                        orderService.complete(testOrder.getId());
                        success.incrementAndGet();
                    } catch (Exception _) {
                        failed.incrementAndGet();
                    } finally {
                        finishLatch.countDown();
                    }
                });
            }

            startLatch.countDown();
            finishLatch.await();
        }

        Order updated = orderRepository.findById(testOrder.getId()).orElseThrow();

        assertEquals(OrderStatus.COMPLETED, updated.getStatus());
        assertEquals(1, success.get(), "Только один поток должен обновить версию успешно");
        assertEquals(1, failed.get(), "Второй поток должен получить OptimisticLockException");
    }
}