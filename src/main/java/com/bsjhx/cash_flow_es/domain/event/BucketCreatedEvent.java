package com.bsjhx.cash_flow_es.domain.event;

import com.bsjhx.cash_flow_es.domain.Event;
import lombok.Getter;

import java.util.UUID;

@Getter
public class BucketCreatedEvent implements Event {

    private final UUID id;

    public BucketCreatedEvent() {
        this.id = UUID.randomUUID();
    }
    
    public static BucketCreatedEvent createEvent() {
        return new BucketCreatedEvent();
    }
}
