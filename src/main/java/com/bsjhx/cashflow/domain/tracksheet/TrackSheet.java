package com.bsjhx.cashflow.domain.tracksheet;

import com.bsjhx.cashflow.domain.tracksheet.exception.BucketExceptionReasons;
import com.bsjhx.cashflow.domain.tracksheet.exception.BucketMutationException;
import com.bsjhx.cashflow.domain.common.Event;
import com.bsjhx.cashflow.domain.tracksheet.event.TrackSheetCreatedEvent;
import com.bsjhx.cashflow.domain.tracksheet.event.MoneyTransferredEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class TrackSheet {

    private final UUID id;
    private final Money balance;

    private final List<Event> uncommittedEvents = new ArrayList<>();

    private TrackSheet(UUID id, Money balance) {
        this.id = id;
        this.balance = balance;
    }

    public static TrackSheet fromEvents(List<Event> events) {
        var bucket = new TrackSheet(null, null);

        for (Event event : events) {
            bucket = bucket.mutate(event);
        }

        return bucket;
    }

    public static TrackSheet createNew(UUID bucketId, List<Event> events) {
        var bucket = fromEvents(events);
        var event = TrackSheetCreatedEvent.createEvent(bucketId);
        bucket = bucket.mutate(event);
        bucket.uncommittedEvents.add(event);
        
        return bucket;
    }

    private TrackSheet mutate(Event event) {
        switch (event) {
            case TrackSheetCreatedEvent bucketCreatedEvent -> {
                if (this.id != null) {
                    throw new BucketMutationException(BucketExceptionReasons.BUCKET_ALREADY_OPENED);
                }
                return new TrackSheet(bucketCreatedEvent.getTrackSheetId(), Money.of(0.0));
            }
            case MoneyTransferredEvent moneyTransferredEvent -> {
                if (this.id == null) {
                    throw new BucketMutationException(BucketExceptionReasons.BUCKET_NOT_OPENED);
                }
                if (this.id != moneyTransferredEvent.getTrackSheetId()) {
                    throw new BucketMutationException(BucketExceptionReasons.BUCKET_ID_NOT_MATCHED); 
                }
                return new TrackSheet(this.id, this.balance.add(moneyTransferredEvent.getAmount()));
            }
            default -> throw new IllegalStateException("Event does not exist: " + event);
        }
    }

    public void transfer(Double amount) {
        var moneyAmount = Money.of(amount);
        var event = MoneyTransferredEvent.createEvent(this.id, moneyAmount);
        mutate(event);
        uncommittedEvents.add(event);
    }

    public void markChangesAsCommitted() {
        this.uncommittedEvents.clear();
    }
}
