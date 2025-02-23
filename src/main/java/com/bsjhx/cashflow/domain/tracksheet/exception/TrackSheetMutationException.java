package com.bsjhx.cashflow.domain.tracksheet.exception;

import com.bsjhx.cashflow.domain.common.DomainException;

public class TrackSheetMutationException extends DomainException {
    
    public TrackSheetMutationException(String reason) {
        super(reason);
    }
}
