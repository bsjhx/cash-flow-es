package com.bsjhx.cashflow.application.tracksheet.command;

import java.util.UUID;

public interface TrackSheetCommands {
    record OpenTrackSheetCommand(UUID trackSheetId) {
    }

    record TransferMoneyCommand(UUID trackSheetId, Double amount) {
    }
} 
