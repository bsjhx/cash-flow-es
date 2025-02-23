package com.bsjhx.cashflow.application.tracksheet.command;

import com.bsjhx.cashflow.adapters.outbound.EventStore;
import com.bsjhx.cashflow.application.tracksheet.command.TrackSheetCommands.OpenTrackSheetCommand;
import com.bsjhx.cashflow.domain.tracksheet.TrackSheet;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OpenBucketCommandHandler {

    private final EventStore eventStore;

    public void handle(OpenTrackSheetCommand openBucketCommand) {
        var pastEvents = eventStore.loadEvents(openBucketCommand.trackSheetId()).orElse(List.of());
        var newBucket = TrackSheet.createNew(openBucketCommand.trackSheetId(), pastEvents);
        
        eventStore.saveEvents(newBucket.getId(), newBucket.getUncommittedEvents());
        newBucket.markChangesAsCommitted();
    }
}
