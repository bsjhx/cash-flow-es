package com.bsjhx.cashflow.domain.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

@Getter
@EqualsAndHashCode
public abstract class Event {
    
    private final Instant createdAt;

    public Event(Instant emittedAt) {
        this.createdAt = emittedAt;
    }
}
