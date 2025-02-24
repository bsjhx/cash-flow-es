package com.bsjhx.cashflow.adapters.inbound.rest.request;

import java.util.UUID;

public record TransferMoneyRequest(UUID trackSheetId, Double amount) {
}
