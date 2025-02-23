package com.bsjhx.cashflow.domain.tracksheet;

import com.bsjhx.cashflow.domain.tracksheet.exception.TrackSheetExceptionReasons;
import com.bsjhx.cashflow.domain.tracksheet.exception.TrackSheetMutationException;
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

    private TrackSheet(final UUID id, final Money balance) {
        this.id = id;
        this.balance = balance;
    }

    public static TrackSheet fromEvents(final List<Event> events) {
        var trackSheet = new TrackSheet(null, null);

        for (Event event : events) {
            trackSheet = trackSheet.mutate(event);
        }

        return trackSheet;
    }

    public static TrackSheet createNew(final UUID trackSheetId, final List<Event> events) {
        var trackSheet = fromEvents(events);
        var event = TrackSheetCreatedEvent.createEvent(trackSheetId);
        trackSheet = trackSheet.mutate(event);
        trackSheet.uncommittedEvents.add(event);
        
        return trackSheet;
    }

    private TrackSheet mutate(final Event event) {
        switch (event) {
            case TrackSheetCreatedEvent trackSheetCreatedEvent -> {
                if (this.id != null) {
                    throw new TrackSheetMutationException(TrackSheetExceptionReasons.TRACK_SHEET_ALREADY_OPENED);
                }
                return new TrackSheet(trackSheetCreatedEvent.getTrackSheetId(), Money.of(0.0));
            }
            case MoneyTransferredEvent moneyTransferredEvent -> {
                if (this.id == null) {
                    throw new TrackSheetMutationException(TrackSheetExceptionReasons.TRACK_SHEET_NOT_OPENED);
                }
                if (this.id != moneyTransferredEvent.getTrackSheetId()) {
                    throw new TrackSheetMutationException(TrackSheetExceptionReasons.TRACK_SHEET_ID_NOT_MATCHED); 
                }
                return new TrackSheet(this.id, this.balance.add(moneyTransferredEvent.getAmount()));
            }
            default -> throw new IllegalStateException("Event does not exist: " + event);
        }
    }

    public void transfer(final Double amount) {
        var moneyAmount = Money.of(amount);
        var event = MoneyTransferredEvent.createEvent(this.id, moneyAmount);
        mutate(event);
        uncommittedEvents.add(event);
    }

    public void markChangesAsCommitted() {
        this.uncommittedEvents.clear();
    }
}
