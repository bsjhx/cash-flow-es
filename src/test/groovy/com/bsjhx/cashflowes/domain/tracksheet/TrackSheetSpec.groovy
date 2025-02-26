package com.bsjhx.cashflowes.domain.tracksheet

import com.bsjhx.cashflow.domain.tracksheet.TrackSheet
import com.bsjhx.cashflow.domain.tracksheet.Money
import com.bsjhx.cashflow.domain.tracksheet.event.TrackSheetCreatedEvent
import com.bsjhx.cashflow.domain.tracksheet.event.MoneyTransferredEvent
import com.bsjhx.cashflow.domain.tracksheet.exception.TrackSheetExceptionReasons
import com.bsjhx.cashflow.domain.tracksheet.exception.TrackSheetMutationException
import spock.lang.Specification

import java.time.Instant

class TrackSheetSpec extends Specification {

    def "should create simple track sheet"() {
        given:
            def trackSheetId = UUID.randomUUID()
            def event = TrackSheetCreatedEvent.createEvent(trackSheetId)

        when:
            def trackSheet = TrackSheet.fromEvents([event])

        then:
            trackSheet.id == trackSheetId
            trackSheet.balance == Money.of(0.0)
            trackSheet.createdAt.isBefore(Instant.now())
    }

    def "should create simple track sheet and change amount due to next events"() {
        given:
            def trackSheetId = UUID.randomUUID()
            def events = []
            events.add(TrackSheetCreatedEvent.createEvent(trackSheetId))
            events.add(MoneyTransferredEvent.createEvent(trackSheetId, Money.of(5.0)))
            events.add(MoneyTransferredEvent.createEvent(trackSheetId, Money.of(15.0)))
            events.add(MoneyTransferredEvent.createEvent(trackSheetId, Money.of(60.0)))
            events.add(MoneyTransferredEvent.createEvent(trackSheetId, Money.of(-30.0)))

        when:
            def trackSheet = TrackSheet.fromEvents(events)

        then:
            trackSheet.id == trackSheetId
            trackSheet.balance == Money.of(50.0)
    }

    def "should throw exception if opened trackSheet is reopened"() {
        given:
            def trackSheetId = UUID.randomUUID()
            def events = []
            events.add(TrackSheetCreatedEvent.createEvent(trackSheetId))
            events.add(MoneyTransferredEvent.createEvent(trackSheetId, Money.of(5.0)))
            events.add(MoneyTransferredEvent.createEvent(trackSheetId, Money.of(15.0)))
            events.add(MoneyTransferredEvent.createEvent(trackSheetId, Money.of(60.0)))
            events.add(TrackSheetCreatedEvent.createEvent(trackSheetId))

        when:
            TrackSheet.fromEvents(events)

        then:
            def e = thrown(TrackSheetMutationException)
            e.message == TrackSheetExceptionReasons.TRACK_SHEET_ALREADY_OPENED
    }

    def "should throw exception when transferring money to not opened trackSheet"() {
        given:
            def trackSheetId = UUID.randomUUID()
            def events = []
            events.add(MoneyTransferredEvent.createEvent(trackSheetId, Money.of(5.0)))
            events.add(TrackSheetCreatedEvent.createEvent(trackSheetId))
            events.add(MoneyTransferredEvent.createEvent(trackSheetId, Money.of(15.0)))
            events.add(MoneyTransferredEvent.createEvent(trackSheetId, Money.of(60.0)))

        when:
            TrackSheet.fromEvents(events)

        then:
            def e = thrown(TrackSheetMutationException)
            e.message == TrackSheetExceptionReasons.TRACK_SHEET_NOT_OPENED
    }

    def "should throw exception when transfer event trackSheet id is not matched with trackSheet id"() {
        given:
            def trackSheetId = UUID.randomUUID()
            def trackSheetId2 = UUID.randomUUID()
            def events = []
            events.add(TrackSheetCreatedEvent.createEvent(trackSheetId))
            events.add(MoneyTransferredEvent.createEvent(trackSheetId2, Money.of(5.0)))

        when:
            TrackSheet.fromEvents(events)

        then:
            def e = thrown(TrackSheetMutationException)
            e.message == TrackSheetExceptionReasons.TRACK_SHEET_ID_NOT_MATCHED
    }

    def "should create track sheet and time creation must not be changed"() {
        given:
            // create TS and save createdAt
            def trackSheetId = UUID.randomUUID()
            def events = []
            events.add(TrackSheetCreatedEvent.createEvent(trackSheetId))
            def trackSheet = TrackSheet.fromEvents(events)
            def createdAt = trackSheet.createdAt 

        when:
            events.add(MoneyTransferredEvent.createEvent(trackSheetId, Money.of(5.0)))
            events.add(MoneyTransferredEvent.createEvent(trackSheetId, Money.of(15.0)))
            events.add(MoneyTransferredEvent.createEvent(trackSheetId, Money.of(60.0)))
            events.add(MoneyTransferredEvent.createEvent(trackSheetId, Money.of(-30.0)))

            trackSheet = TrackSheet.fromEvents(events)

        then:
            trackSheet.id == trackSheetId
            trackSheet.balance == Money.of(50.0)
            trackSheet.createdAt == createdAt
    }
}
