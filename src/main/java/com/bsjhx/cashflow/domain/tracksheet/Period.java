package com.bsjhx.cashflow.domain.tracksheet;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public final class Period {
    
    private final UUID id;
    private final Instant startedAt;
    private final String name;
}
