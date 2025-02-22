package com.bsjhx.cashflow.application.bucket.query;

import com.bsjhx.cashflow.adapters.outbound.EventStore;
import com.bsjhx.cashflow.domain.tracksheet.Money;
import com.bsjhx.cashflow.domain.tracksheet.projections.CurrentBalanceProjection;
import com.bsjhx.cashflow.domain.common.Event;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CurrentBalanceQueryHandler {

    private final EventStore eventStore;

    public Money getCurrentBalance(CurrentBalanceQuery query) {
        List<Event> events = eventStore.loadEvents(query.bucketId())
                .orElseThrow(() -> new IllegalArgumentException("Bucket not found"));
        var currentBalanceProjection = new CurrentBalanceProjection();

        return currentBalanceProjection.getCurrentBalance(events);
    }
}
