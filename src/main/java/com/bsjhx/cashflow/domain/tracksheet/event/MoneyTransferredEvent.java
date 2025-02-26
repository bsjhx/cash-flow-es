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
    
    private final UUID id;
    private final UUID trackSheetId;
    private final Money amount;

    private MoneyTransferredEvent(final Instant createdAt, final UUID id, final UUID trackSheetId, final Money amount) {
        super(createdAt);
        this.id = id;
        this.trackSheetId = trackSheetId;
        this.amount = amount;
    }

    public static MoneyTransferredEvent createEvent(final UUID trackSheetId, final Money amount) {
        return new MoneyTransferredEvent(Instant.now(), UUID.randomUUID(), trackSheetId, amount);
    }
}
