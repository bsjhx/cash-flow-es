package com.bsjhx.cash_flow_es.domain.common;

public abstract class DomainException extends RuntimeException {
  
    public DomainException(String message) {
        super(message);
    }
}
