package com.bsjhx.cashflow.adapters.inbound.rest;

import com.bsjhx.cashflow.adapters.inbound.rest.request.TransferMoneyRequest;
import com.bsjhx.cashflow.adapters.inbound.rest.response.BucketIdResponse;
import com.bsjhx.cashflow.adapters.outbound.EventStore;
import com.bsjhx.cashflow.application.bucket.command.BucketCommands.OpenBucketCommand;
import com.bsjhx.cashflow.application.bucket.command.BucketCommands.TransferMoneyCommand;
import com.bsjhx.cashflow.application.bucket.command.MoneyTransferCommandHandler;
import com.bsjhx.cashflow.application.bucket.command.OpenBucketCommandHandler;
import com.bsjhx.cashflow.application.bucket.query.CurrentBalanceQuery;
import com.bsjhx.cashflow.application.bucket.query.CurrentBalanceQueryHandler;
import com.bsjhx.cashflow.domain.tracksheet.Money;
import com.bsjhx.cashflow.domain.common.Event;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/bucket")
@AllArgsConstructor
public class BucketController {
    
    private final OpenBucketCommandHandler openBucketCommandHandler;
    private final MoneyTransferCommandHandler moneyTransferCommandHandler;
    private final CurrentBalanceQueryHandler currentBalanceQueryHandler;
    private final EventStore eventStore;
    
    @PostMapping("/create")
    public BucketIdResponse createBucket() {
        var createNewBucketCommand = new OpenBucketCommand(UUID.randomUUID());
        openBucketCommandHandler.handle(createNewBucketCommand);
        
        return new BucketIdResponse(createNewBucketCommand.bucketId());
    }

    @PostMapping("/transfer")
    public BucketIdResponse transferCash(@RequestBody TransferMoneyRequest request) {
        var transferMoneyCommand = new TransferMoneyCommand(request.bucketId(), request.amount());
        moneyTransferCommandHandler.handle(transferMoneyCommand);

        return new BucketIdResponse(transferMoneyCommand.bucketId());
    }
    
    @GetMapping("/all-events/{bucketId}")
    public List<Event> getAllEvents(@PathVariable UUID bucketId) {
        return eventStore.loadEvents(bucketId).orElseThrow(() -> new IllegalArgumentException("not found"));
    }

    @GetMapping("/current-balance/{bucketId}")
    public Money getCurrentBalance(@PathVariable UUID bucketId) {
        var query = new CurrentBalanceQuery(bucketId);
        return currentBalanceQueryHandler.getCurrentBalance(query);
    }
}
