package com.bsjhx.cashflow.domain.tracksheet;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Money {
    
    private final Double amount;

    private Money(final Double amount) {
        this.amount = amount;
    }

    public static Money of(final Double amount) {
        return new Money(amount);
    }

    public static Money zero() {
        return Money.of(0.0);
    }

    public Money add(final Money money) {
        return new Money(this.amount + money.amount);
    }

}
