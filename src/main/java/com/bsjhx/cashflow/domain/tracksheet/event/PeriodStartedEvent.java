package com.bsjhx.cashflow.domain.tracksheet.event;

import com.bsjhx.cashflow.domain.common.Event;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PeriodStartedEvent extends Event {
    
    private String name;
    
    public PeriodStartedEvent of(final String name) {
        return new PeriodStartedEvent(name);
    }

}
