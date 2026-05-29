package com.p2p.oms.ad.event.listener;

import com.p2p.oms.ad.event.domain.AdCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component

public class AdCreatedLoggingListener {

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(
            AdCreatedEvent event
    ) {

        log.info(
                "Ad created: adId={}, userId={}",
                event.adId(),
                event.userId()
        );
    }
}