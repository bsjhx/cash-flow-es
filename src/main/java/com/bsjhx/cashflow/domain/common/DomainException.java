package com.bsjhx.cashflow.domain.common;

public abstract class DomainException extends RuntimeException {
  
    public DomainException(String message) {
        super(message);
    }
}
