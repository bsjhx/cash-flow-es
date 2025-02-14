package com.bsjhx.cashflow.domain.bucket.event;

import com.bsjhx.cashflow.domain.common.Event;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class BucketCreatedEvent implements Event {

    private final UUID id;
    private final UUID bucketId;

    private BucketCreatedEvent(UUID bucketId) {
        this.id = UUID.randomUUID();
        this.bucketId = bucketId;
    }
    
    public static BucketCreatedEvent createEvent(UUID bucketId) {
        return new BucketCreatedEvent(bucketId);
    }
}
