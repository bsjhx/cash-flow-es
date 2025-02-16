package com.bsjhx.cashflow.application.bucket.command;

import com.bsjhx.cashflow.adapters.outbound.EventStore;
import com.bsjhx.cashflow.application.bucket.command.BucketCommands.TransferMoneyCommand;
import com.bsjhx.cashflow.domain.bucket.Bucket;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MoneyTransferCommandHandler {

    private final EventStore eventStore;

    public void handle(TransferMoneyCommand transferMoneyCommand) {
        var pastEvents = eventStore.loadEvents(transferMoneyCommand.bucketId())
                .orElseThrow(() -> new IllegalArgumentException("Bucket not found"));

        var bucket = Bucket.fromEvents(pastEvents);
        
        bucket.transfer(transferMoneyCommand.amount());
        
        eventStore.saveEvents(bucket.getId(), bucket.getUncommittedEvents());
        bucket.markChangesAsCommitted();
    }

}
