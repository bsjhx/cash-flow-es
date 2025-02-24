package com.bsjhx.cashflow.domain.tracksheet.projections;

import com.bsjhx.cashflow.domain.tracksheet.Money;
import com.bsjhx.cashflow.domain.tracksheet.event.MoneyTransferredEvent;
import com.bsjhx.cashflow.domain.common.Event;

import java.util.List;
import java.util.Objects;

public class CurrentBalanceProjection {

    public Money getCurrentBalance(final List<Event> events) {
        var currentBalance = Money.zero();

        for (Event event : events) {
            if (Objects.requireNonNull(event) instanceof MoneyTransferredEvent moneyTransferredEvent) {
                currentBalance = currentBalance.add(moneyTransferredEvent.getAmount());
            }
        }

        return currentBalance;
    }

}
