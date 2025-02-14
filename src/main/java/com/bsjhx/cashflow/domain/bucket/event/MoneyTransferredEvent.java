package com.bsjhx.cashflow.domain.bucket.event;

import com.bsjhx.cashflow.domain.common.Event;
import com.bsjhx.cashflow.domain.bucket.Money;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class MoneyTransferredEvent implements Event {
    
    private final UUID id;
    private final UUID bucketId;
    private final Money amount;

    private MoneyTransferredEvent(UUID id, UUID bucketId, Money amount) {
        this.id = id;
        this.bucketId = bucketId;
        this.amount = amount;
    }

    public static MoneyTransferredEvent createEvent(UUID bucketId, Money amount) {
        return new MoneyTransferredEvent(UUID.randomUUID(), bucketId, amount);
    }
}
