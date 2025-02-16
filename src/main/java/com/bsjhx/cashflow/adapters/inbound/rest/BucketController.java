package com.bsjhx.cashflow.adapters.inbound.rest;

import com.bsjhx.cashflow.adapters.inbound.rest.response.BucketCreatedResponse;
import com.bsjhx.cashflow.adapters.outbound.EventStore;
import com.bsjhx.cashflow.application.bucket.command.BucketCommands;
import com.bsjhx.cashflow.application.bucket.command.BucketCommands.OpenBucketCommand;
import com.bsjhx.cashflow.application.bucket.command.OpenBucketCommandHandler;
import com.bsjhx.cashflow.domain.common.Event;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bucket")
@AllArgsConstructor
public class BucketController {
    
    private final OpenBucketCommandHandler openBucketCommandHandler;
    private final EventStore eventStore;
    
    @PostMapping("/create")
    public BucketCreatedResponse createBucket() {
        var createNewBucketCommand = new OpenBucketCommand(UUID.randomUUID());
        openBucketCommandHandler.handle(createNewBucketCommand);
        
        return new BucketCreatedResponse(createNewBucketCommand.bucketId());
    }
    
    @GetMapping("/all-events/{bucketId}")
    public List<Event> getAllEvents(@PathVariable UUID bucketId) {
        return eventStore.loadEvents(bucketId).orElseThrow(() -> new IllegalArgumentException("not found"));
    }
}
