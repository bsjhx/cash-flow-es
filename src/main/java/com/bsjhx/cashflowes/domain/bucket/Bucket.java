package com.bsjhx.cashflowes.domain.bucket;

import com.bsjhx.cash_flow_es.domain.Event;
import com.bsjhx.cash_flow_es.domain.event.BucketCreatedEvent;
import com.bsjhx.cash_flow_es.domain.event.MoneyTransferredEvent;
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
        if (event instanceof BucketCreatedEvent bucketCreatedEvent) {
            return new Bucket(bucketCreatedEvent.getId(), Money.of(0.0));
        } else if (event instanceof MoneyTransferredEvent moneyTransferredEvent) {
            return new Bucket(this.id, this.balance.add(moneyTransferredEvent.getAmount()));
        }

        throw new IllegalArgumentException("Event does not exist");
    }

}
