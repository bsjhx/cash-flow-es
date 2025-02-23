package com.bsjhx.cashflow.infrastructure.eventstore

import com.bsjhx.cashflow.adapters.outbound.EventStore
import com.bsjhx.cashflow.domain.tracksheet.Money
import com.bsjhx.cashflow.domain.tracksheet.event.TrackSheetCreatedEvent
import com.bsjhx.cashflow.domain.tracksheet.event.MoneyTransferredEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class InMemoryEventStoreSpec extends Specification {

    @Autowired
    private EventStore eventStore

    def "should return empty if no events added"() {
        when:
        def result = eventStore.loadEvents(UUID.randomUUID())

        then:
        Optional.empty() == result
    }

    def "should add events and then read them"() {
        given:
        def trackSheetId = UUID.randomUUID()
        def events = [
                TrackSheetCreatedEvent.createEvent(trackSheetId),
                MoneyTransferredEvent.createEvent(trackSheetId, Money.of(10.0))
        ]

        when:
        eventStore.saveEvents(trackSheetId, events)

        then:
        def result = eventStore.loadEvents(trackSheetId)
        result.isPresent()
        2 == result.get().size()
    }

    def "should add events few times and keep previous one"() {
        given:
        def trackSheetId = UUID.randomUUID()
        def events = [
                TrackSheetCreatedEvent.createEvent(trackSheetId),
                MoneyTransferredEvent.createEvent(trackSheetId, Money.of(10.0))
        ]

        when:
        eventStore.saveEvents(trackSheetId, events)

        then:
        def result = eventStore.loadEvents(trackSheetId)
        result.isPresent()
        2 == result.get().size()

        when:
        def events2 = [
                MoneyTransferredEvent.createEvent(trackSheetId, Money.of(20.0)),
        ]
        eventStore.saveEvents(trackSheetId, events2)

        then:
        def result2 = eventStore.loadEvents(trackSheetId)
        result2.isPresent()
        3 == result2.get().size()
    }

    def "should add events for multiple streams"() {
        given:
        def trackSheetId = UUID.randomUUID()
        def trackSheetId1 = UUID.randomUUID()
        def trackSheetId2 = UUID.randomUUID()

        def events = [
                TrackSheetCreatedEvent.createEvent(trackSheetId),
                MoneyTransferredEvent.createEvent(trackSheetId, Money.of(10.0))
        ]

        def events1 = [
                TrackSheetCreatedEvent.createEvent(trackSheetId1),
                MoneyTransferredEvent.createEvent(trackSheetId1, Money.of(15.0)),
                MoneyTransferredEvent.createEvent(trackSheetId1, Money.of(30.0)),
        ]

        def events2 = [
                TrackSheetCreatedEvent.createEvent(trackSheetId2),
                MoneyTransferredEvent.createEvent(trackSheetId2, Money.of(20.0)),
                MoneyTransferredEvent.createEvent(trackSheetId2, Money.of(40.0)),
                MoneyTransferredEvent.createEvent(trackSheetId2, Money.of(60.0)),
        ]

        when:
        eventStore.saveEvents(trackSheetId, events)
        eventStore.saveEvents(trackSheetId1, events1)
        eventStore.saveEvents(trackSheetId2, events2)

        then:
        def result = eventStore.loadEvents(trackSheetId)
        result.isPresent()
        2 == result.get().size()

        def result1 = eventStore.loadEvents(trackSheetId1)
        result1.isPresent()
        3 == result1.get().size()

        def result2 = eventStore.loadEvents(trackSheetId2)
        result2.isPresent()
        4 == result2.get().size()
    }

}
