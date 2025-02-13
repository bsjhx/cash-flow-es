package com.bsjhx.cashflow.domain.bucket;

import lombok.Getter;

import java.util.Objects;

@Getter
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }
}
