package com.bsjhx.cashflow.infrastructure.eventstore;

import com.bsjhx.cashflow.adapters.outbound.EventStore;
import com.bsjhx.cashflow.domain.common.Event;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InMemoryEventStore implements EventStore {
    
    private final Map<UUID, List<Event>> eventsMap;

    public InMemoryEventStore() {
        this.eventsMap = new HashMap<>();
    }

    @Override
    public Optional<List<Event>> loadEvents(final UUID streamId) {
        if (eventsMap.containsKey(streamId)) {
            return Optional.of(eventsMap.get(streamId));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void saveEvents(final UUID streamId, final List<Event> events) {
        this.eventsMap.computeIfAbsent(streamId, k -> new ArrayList<>()).addAll(events);
    }
}
