package com.bsjhx.cashflow.domain.tracksheet.event;

import com.bsjhx.cashflow.domain.common.Event;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class TrackSheetCreatedEvent implements Event {

    private final UUID id;
    private final UUID trackSheetId;

    private TrackSheetCreatedEvent(UUID trackSheetId) {
        this.id = UUID.randomUUID();
        this.trackSheetId = trackSheetId;
    }
    
    public static TrackSheetCreatedEvent createEvent(UUID trackSheetId) {
        return new TrackSheetCreatedEvent(trackSheetId);
    }
}
