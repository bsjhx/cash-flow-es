package com.bsjhx.cash_flow_es.domain.bucket.exception;

import com.bsjhx.cash_flow_es.domain.common.DomainException;

public class BucketMutationException extends DomainException {
    
    public BucketMutationException(String reason) {
        super(reason);
    }
}
