package com.bsjhx.cashflow.domain.bucket.exception;

import com.bsjhx.cashflow.domain.common.DomainException;

public class BucketMutationException extends DomainException {
    
    public BucketMutationException(String reason) {
        super(reason);
    }
}
