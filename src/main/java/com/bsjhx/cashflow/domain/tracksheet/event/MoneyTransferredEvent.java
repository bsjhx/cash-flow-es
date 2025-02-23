package com.bsjhx.cashflow.domain.tracksheet.event;

import com.bsjhx.cashflow.domain.common.Event;
import com.bsjhx.cashflow.domain.tracksheet.Money;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public final class MoneyTransferredEvent implements Event {
    
    private final UUID id;
    private final UUID trackSheetId;
    private final Money amount;

    private MoneyTransferredEvent(final UUID id, final UUID trackSheetId, final Money amount) {
        this.id = id;
        this.trackSheetId = trackSheetId;
        this.amount = amount;
    }

    public static MoneyTransferredEvent createEvent(final UUID trackSheetId, final Money amount) {
        return new MoneyTransferredEvent(UUID.randomUUID(), trackSheetId, amount);
    }
}
