package com.bsjhx.cashflow.adapters.inbound.rest;

import com.bsjhx.cashflow.adapters.inbound.rest.request.TransferMoneyRequest;
import com.bsjhx.cashflow.adapters.inbound.rest.response.TrackSheetIdIdResponse;
import com.bsjhx.cashflow.adapters.outbound.EventStore;
import com.bsjhx.cashflow.application.tracksheet.command.TrackSheetCommands.OpenTrackSheetCommand;
import com.bsjhx.cashflow.application.tracksheet.command.TrackSheetCommands.TransferMoneyCommand;
import com.bsjhx.cashflow.application.tracksheet.command.MoneyTransferCommandHandler;
import com.bsjhx.cashflow.application.tracksheet.command.OpenTrackSheetCommandHandler;
import com.bsjhx.cashflow.application.tracksheet.query.CurrentBalanceQuery;
import com.bsjhx.cashflow.application.tracksheet.query.CurrentBalanceQueryHandler;
import com.bsjhx.cashflow.domain.tracksheet.Money;
import com.bsjhx.cashflow.domain.common.Event;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/track-sheet")
@AllArgsConstructor
public class TrackSheetController {
    
    private final OpenTrackSheetCommandHandler openTrackSheetCommandHandler;
    private final MoneyTransferCommandHandler moneyTransferCommandHandler;
    private final CurrentBalanceQueryHandler currentBalanceQueryHandler;
    private final EventStore eventStore;
    
    @PostMapping("/create")
    public TrackSheetIdIdResponse createTrackSheet() {
        var openTrackSheetCommand = new OpenTrackSheetCommand(UUID.randomUUID());
        openTrackSheetCommandHandler.handle(openTrackSheetCommand);
        
        return new TrackSheetIdIdResponse(openTrackSheetCommand.trackSheetId());
    }

    @PostMapping("/transfer")
    public TrackSheetIdIdResponse transferCash(@RequestBody TransferMoneyRequest request) {
        var transferMoneyCommand = new TransferMoneyCommand(request.trackSheetId(), request.amount());
        moneyTransferCommandHandler.handle(transferMoneyCommand);

        return new TrackSheetIdIdResponse(transferMoneyCommand.trackSheetId());
    }
    
    @GetMapping("/all-events/{trackSheetId}")
    public List<Event> getAllEvents(@PathVariable UUID trackSheetId) {
        return eventStore.loadEvents(trackSheetId).orElseThrow(() -> new IllegalArgumentException("not found"));
    }

    @GetMapping("/current-balance/{trackSheetId}")
    public Money getCurrentBalance(@PathVariable UUID trackSheetId) {
        var query = new CurrentBalanceQuery(trackSheetId);
        return currentBalanceQueryHandler.getCurrentBalance(query);
    }
}
