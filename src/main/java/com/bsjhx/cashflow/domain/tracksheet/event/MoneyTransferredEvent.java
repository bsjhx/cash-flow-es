package com.bsjhx.cashflow.domain.tracksheet.event;

import com.bsjhx.cashflow.domain.common.Event;
import com.bsjhx.cashflow.domain.tracksheet.Money;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class MoneyTransferredEvent implements Event {
    
    private final UUID id;
    private final UUID trackSheetId;
    private final Money amount;

    private MoneyTransferredEvent(UUID id, UUID trackSheetId, Money amount) {
        this.id = id;
        this.trackSheetId = trackSheetId;
        this.amount = amount;
    }

    public static MoneyTransferredEvent createEvent(UUID trackSheetId, Money amount) {
        return new MoneyTransferredEvent(UUID.randomUUID(), trackSheetId, amount);
    }
}
