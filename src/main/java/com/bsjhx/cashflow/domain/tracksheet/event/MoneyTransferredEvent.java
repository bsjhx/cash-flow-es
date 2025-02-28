package com.bsjhx.cashflow.domain.tracksheet.event;

import com.bsjhx.cashflow.domain.common.Event;
import com.bsjhx.cashflow.domain.tracksheet.Money;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class MoneyTransferredEvent extends Event {
    
    private final UUID trackSheetId;
    private final Money amount;

    private MoneyTransferredEvent(final UUID trackSheetId, final Money amount) {
        this.trackSheetId = trackSheetId;
        this.amount = amount;
    }

    public static MoneyTransferredEvent createEvent(final UUID trackSheetId, final Money amount) {
        return new MoneyTransferredEvent(trackSheetId, amount);
    }
}
