package com.bsjhx.cashflowes.domain.tracksheet

import com.bsjhx.cashflow.domain.tracksheet.TrackSheet
import com.bsjhx.cashflow.domain.tracksheet.Money
import com.bsjhx.cashflow.domain.tracksheet.event.TrackSheetCreatedEvent
import com.bsjhx.cashflow.domain.tracksheet.event.MoneyTransferredEvent
import com.bsjhx.cashflow.domain.tracksheet.exception.BucketExceptionReasons
import com.bsjhx.cashflow.domain.tracksheet.exception.BucketMutationException
import spock.lang.Specification

class BucketSpec extends Specification {

    def "should init simple bucket"() {
        given:
            def bucketId = UUID.randomUUID()
            def event = TrackSheetCreatedEvent.createEvent(bucketId)

        when:
            def bucket = TrackSheet.fromEvents([event])

        then:
            bucket.id == bucketId
            bucket.balance == Money.of(0.0)
    }

    def "should init simple bucket and change amount due to next events"() {
        given:
            def bucketId = UUID.randomUUID()
            def events = []
            events.add(TrackSheetCreatedEvent.createEvent(bucketId))
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(5.0)))
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(15.0)))
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(60.0)))
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(-30.0)))

        when:
            def bucket = TrackSheet.fromEvents(events)

        then:
            bucket.id == bucketId
            bucket.balance == Money.of(50.0)
    }

    def "should throw exception if opened bucket is reopened"() {
        given:
            def bucketId = UUID.randomUUID()
            def events = []
            events.add(TrackSheetCreatedEvent.createEvent(bucketId))
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(5.0)))
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(15.0)))
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(60.0)))
            events.add(TrackSheetCreatedEvent.createEvent(bucketId))

        when:
            TrackSheet.fromEvents(events)

        then:
            def e = thrown(BucketMutationException)
            e.message == BucketExceptionReasons.BUCKET_ALREADY_OPENED
    }

    def "should throw exception when transferring money to not opened bucket"() {
        given:
            def bucketId = UUID.randomUUID()
            def events = []
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(5.0)))
            events.add(TrackSheetCreatedEvent.createEvent(bucketId))
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(15.0)))
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(60.0)))

        when:
            TrackSheet.fromEvents(events)

        then:
            def e = thrown(BucketMutationException)
            e.message == BucketExceptionReasons.BUCKET_NOT_OPENED
    }

    def "should throw exception when transfer event bucket id is not matched with bucket id"() {
        given:
            def bucketId = UUID.randomUUID()
            def bucketId2 = UUID.randomUUID()
            def events = []
            events.add(TrackSheetCreatedEvent.createEvent(bucketId))
            events.add(MoneyTransferredEvent.createEvent(bucketId2, Money.of(5.0)))

        when:
            TrackSheet.fromEvents(events)

        then:
            def e = thrown(BucketMutationException)
            e.message == BucketExceptionReasons.BUCKET_ID_NOT_MATCHED
    }
}
