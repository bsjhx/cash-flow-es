package com.bsjhx.cashflowes.domain.bucket;

import com.bsjhx.cash_flow_es.domain.bucket.exception.BucketReopenedException;
import com.bsjhx.cash_flow_es.domain.common.Event;
import com.bsjhx.cash_flow_es.domain.bucket.event.BucketCreatedEvent;
import com.bsjhx.cash_flow_es.domain.bucket.event.MoneyTransferredEvent;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Bucket {

    private final UUID id;
    private final Money balance;

    private Bucket(UUID id, Money balance) {
        this.id = id;
        this.balance = balance;
    }

    public static Bucket fromEvents(List<Event> events) {
        var bucket = new Bucket(null, null);

        for (Event event : events) {
            bucket = bucket.mutate(event);
        }

        return bucket;
    }

    private Bucket mutate(Event event) {
        switch (event) {
            case BucketCreatedEvent bucketCreatedEvent -> {
                if (this.id != null) {
                    throw new BucketReopenedException();
                }
                return new Bucket(bucketCreatedEvent.getId(), Money.of(0.0));
            }
            case MoneyTransferredEvent moneyTransferredEvent -> {
                return new Bucket(this.id, this.balance.add(moneyTransferredEvent.getAmount()));
            }
            default -> throw new IllegalStateException("Event does not exist: " + event);
        }
    }

}
