package com.bsjhx.cashflow.domain.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode
public abstract class Event {
    
    private final UUID id;
    private final Instant createdAt;

    public Event() {
        this.createdAt = Instant.now();
        this.id = UUID.randomUUID();
    }
}
