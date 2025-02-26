package com.bsjhx.cashflow.domain.tracksheet.event;

import com.bsjhx.cashflow.domain.common.Event;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class TrackSheetCreatedEvent extends Event {

    private final UUID id;
    private final UUID trackSheetId;

    private TrackSheetCreatedEvent(final Instant createdAt, final UUID trackSheetId) {
        super(createdAt);
        this.id = UUID.randomUUID();
        this.trackSheetId = trackSheetId;
    }
    
    public static TrackSheetCreatedEvent createEvent(final UUID trackSheetId) {
        return new TrackSheetCreatedEvent(Instant.now(), trackSheetId);
    }
}
