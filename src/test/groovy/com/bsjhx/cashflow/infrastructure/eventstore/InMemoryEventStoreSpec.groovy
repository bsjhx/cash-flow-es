package com.bsjhx.cashflow.infrastructure.eventstore

import com.bsjhx.cashflow.adapters.outbound.EventStore
import com.bsjhx.cashflow.domain.bucket.Money
import com.bsjhx.cashflow.domain.bucket.event.BucketCreatedEvent
import com.bsjhx.cashflow.domain.bucket.event.MoneyTransferredEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class InMemoryEventStoreSpec extends Specification {
    
    @Autowired
    private EventStore eventStore;

    def "should return empty if no events added"() {
        when:
            def result = eventStore.loadEvents(UUID.randomUUID())

        then:
            Optional.empty() == result
    }

    def "should add events and then read them"() {
        given:
            def bucketId = UUID.randomUUID()
            def events = [
                    BucketCreatedEvent.createEvent(bucketId),
                    MoneyTransferredEvent.createEvent(bucketId, Money.of(10.0))
            ]
        
        when:
            eventStore.saveEvents(bucketId, events)
        
        then:
            def result = eventStore.loadEvents(bucketId)
            result.isPresent()
            2 == result.get().size()
    }

}
