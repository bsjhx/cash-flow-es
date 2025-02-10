package com.bsjhx.cash_flow_es.domain.bucket;

import lombok.Getter;

@Getter
public class Money {
    
    private final Double amount;

    private Money(Double amount) {
        this.amount = amount;
    }

    static Money of(Double amount) {
        return new Money(amount);
    }

    public Money add(Money money) {
        return new Money(this.amount + money.amount);
    }
}
