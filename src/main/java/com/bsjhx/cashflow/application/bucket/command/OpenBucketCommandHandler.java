package com.bsjhx.cashflow.application.bucket.command;

import com.bsjhx.cashflow.adapters.outbound.EventStore;
import com.bsjhx.cashflow.application.bucket.command.BucketCommands.OpenBucketCommand;
import com.bsjhx.cashflow.domain.tracksheet.Bucket;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OpenBucketCommandHandler {

    private final EventStore eventStore;

    public void handle(OpenBucketCommand openBucketCommand) {
        var pastEvents = eventStore.loadEvents(openBucketCommand.bucketId()).orElse(List.of());
        var newBucket = Bucket.createNew(openBucketCommand.bucketId(), pastEvents);
        
        eventStore.saveEvents(newBucket.getId(), newBucket.getUncommittedEvents());
        newBucket.markChangesAsCommitted();
    }
}
