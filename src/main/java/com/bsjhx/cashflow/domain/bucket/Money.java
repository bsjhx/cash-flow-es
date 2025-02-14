package com.bsjhx.cashflow.domain.bucket;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@EqualsAndHashCode
public class Money {
    
    private final Double amount;

    private Money(Double amount) {
        this.amount = amount;
    }

    public static Money of(Double amount) {
        return new Money(amount);
    }

    public Money add(Money money) {
        return new Money(this.amount + money.amount);
    }

}
