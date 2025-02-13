package com.bsjhx.cashflow.domain.bucket.event;

import com.bsjhx.cashflow.domain.common.Event;
import lombok.Getter;

import java.util.UUID;

@Getter
public class BucketCreatedEvent implements Event {

    private final UUID id;

    public BucketCreatedEvent(UUID bucketId) {
        this.id = bucketId;
    }
    
    public static BucketCreatedEvent createEvent(UUID bucketId) {
        return new BucketCreatedEvent(bucketId);
    }
}
