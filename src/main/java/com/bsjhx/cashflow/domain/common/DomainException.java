package com.bsjhx.cashflow.domain.common;

public abstract class DomainException extends RuntimeException {
  
    public DomainException(final String message) {
        super(message);
    }
}
