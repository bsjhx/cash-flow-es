package com.bsjhx.cashflow.domain.tracksheet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Period {

    private final UUID id;
    private final Instant startedAt;
    private final String name;

    public static Period of(String name) {
        return new Period(
            UUID.randomUUID(),
                Instant.now(),
                name
        );
    }
}
