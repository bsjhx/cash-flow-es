package com.bsjhx.cashflow.domain.bucket.projections;

import com.bsjhx.cashflow.domain.bucket.Money;
import com.bsjhx.cashflow.domain.bucket.event.MoneyTransferredEvent;
import com.bsjhx.cashflow.domain.common.Event;

import java.util.List;
import java.util.Objects;

public class CurrentBalanceProjection {

    public Money getCurrentBalance(List<Event> events) {
        var currentBalance = Money.zero();

        for (Event event : events) {
            if (Objects.requireNonNull(event) instanceof MoneyTransferredEvent moneyTransferredEvent) {
                currentBalance.add(moneyTransferredEvent.getAmount());
            } else {
                throw new IllegalStateException("Unexpected value: " + event);
            }
        }

        return currentBalance;
    }

}
