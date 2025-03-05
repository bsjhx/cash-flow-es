package com.bsjhx.cashflow.domain.tracksheet.event;

import com.bsjhx.cashflow.domain.common.Event;
import com.bsjhx.cashflow.domain.tracksheet.Money;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class MoneyTransferredEvent extends Event {
    
    private final UUID trackSheetId;
    private final Money amount;

    public static MoneyTransferredEvent createEvent(final UUID trackSheetId, final Money amount) {
        return new MoneyTransferredEvent(trackSheetId, amount);
    }
}
