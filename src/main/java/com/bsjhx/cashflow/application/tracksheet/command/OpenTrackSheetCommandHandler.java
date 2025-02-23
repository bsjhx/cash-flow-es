package com.bsjhx.cashflow.application.tracksheet.command;

import com.bsjhx.cashflow.adapters.outbound.EventStore;
import com.bsjhx.cashflow.application.tracksheet.command.TrackSheetCommands.OpenTrackSheetCommand;
import com.bsjhx.cashflow.domain.tracksheet.TrackSheet;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OpenTrackSheetCommandHandler {

    private final EventStore eventStore;

    public void handle(final OpenTrackSheetCommand openTrackSheetCommand) {
        var pastEvents = eventStore.loadEvents(openTrackSheetCommand.trackSheetId()).orElse(List.of());
        var newTrackSheet = TrackSheet.createNew(openTrackSheetCommand.trackSheetId(), pastEvents);
        
        eventStore.saveEvents(newTrackSheet.getId(), newTrackSheet.getUncommittedEvents());
        newTrackSheet.markChangesAsCommitted();
    }
}
