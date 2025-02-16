package com.bsjhx.cashflow.application.bucket.command;

import java.util.UUID;

public interface BucketCommands {
    record OpenBucketCommand(UUID bucketId) {
    }

    record TransferMoneyCommand(UUID bucketId, Double amount) {
    }
} 
