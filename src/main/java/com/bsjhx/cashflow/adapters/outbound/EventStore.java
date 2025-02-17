package com.bsjhx.cashflow.adapters.outbound;

import com.bsjhx.cashflow.domain.common.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventStore {
    
    Optional<List<Event>> loadEvents(UUID streamId);
    
    void saveEvents(UUID streamId, List<Event> events);
}
