package com.bsjhx.cash_flow_es.domain.bucket.exception;

import com.bsjhx.cash_flow_es.domain.common.DomainException;

public class BucketReopenedException extends DomainException {
    
    public BucketReopenedException() {
        super("Bucket can't ne opened once it was opened");
    }
}
