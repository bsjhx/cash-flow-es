package com.bsjhx.cashflow.adapters.inbound.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/health-check")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<?> getHealthCheck() {
        return ResponseEntity.ok().build();
    }
}
