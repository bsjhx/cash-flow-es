package com.bsjhx.cashflow.domain.bucket;

import com.bsjhx.cashflow.domain.bucket.exception.BucketExceptionReasons;
import com.bsjhx.cashflow.domain.bucket.exception.BucketMutationException;
import com.bsjhx.cashflow.domain.common.Event;
import com.bsjhx.cashflow.domain.bucket.event.BucketCreatedEvent;
import com.bsjhx.cashflow.domain.bucket.event.MoneyTransferredEvent;
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
                    throw new BucketMutationException(BucketExceptionReasons.BUCKET_ALREADY_OPENED);
                }
                return new Bucket(bucketCreatedEvent.getId(), Money.of(0.0));
            }
            case MoneyTransferredEvent moneyTransferredEvent -> {
                if (this.id == null) {
                    throw new BucketMutationException(BucketExceptionReasons.BUCKET_NOT_OPENED);
                }
                if (this.id != moneyTransferredEvent.getBucketId()) {
                    throw new BucketMutationException(BucketExceptionReasons.BUCKET_ID_NOT_MATCHED); 
                }
                return new Bucket(this.id, this.balance.add(moneyTransferredEvent.getAmount()));
            }
            default -> throw new IllegalStateException("Event does not exist: " + event);
        }
    }

}
