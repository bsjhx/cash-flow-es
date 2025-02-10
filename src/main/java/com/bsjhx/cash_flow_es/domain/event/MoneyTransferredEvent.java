package com.bsjhx.cash_flow_es.domain.event;

import com.bsjhx.cash_flow_es.domain.Event;
import com.bsjhx.cash_flow_es.domain.bucket.Money;
import lombok.Getter;

import java.util.UUID;

@Getter
public class MoneyTransferredEvent implements Event {
    
    private final UUID id;
    private final UUID bucketId;
    private final Money amount;

    private MoneyTransferredEvent(UUID id, UUID bucketId, Money amount) {
        this.id = id;
        this.bucketId = bucketId;
        this.amount = amount;
    }

    MoneyTransferredEvent createEvent(UUID bucketId, Money amount) {
        return new MoneyTransferredEvent(UUID.randomUUID(), bucketId, amount);
    }
}
