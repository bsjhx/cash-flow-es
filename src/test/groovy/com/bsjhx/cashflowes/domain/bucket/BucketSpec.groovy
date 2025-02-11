package com.bsjhx.cashflowes.domain.bucket

import com.bsjhx.cash_flow_es.domain.bucket.event.BucketCreatedEvent
import com.bsjhx.cash_flow_es.domain.bucket.event.MoneyTransferredEvent
import com.bsjhx.cash_flow_es.domain.bucket.exception.BucketReopenedException
import spock.lang.Specification

class BucketSpec extends Specification {

    def "should init simple bucket"() {
        given:
            def bucketId = UUID.randomUUID()
            def event = BucketCreatedEvent.createEvent(bucketId)

        when:
            def bucket = Bucket.fromEvents([event])

        then:
            bucket.id == bucketId
            bucket.balance == Money.of(0.0)
    }

    def "should init simple bucket and change amount due to next events"() {
        given:
            def bucketId = UUID.randomUUID()
            def events = []
            events.add(BucketCreatedEvent.createEvent(bucketId))
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(5.0)))
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(15.0)))
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(60.0)))
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(-30.0)))

        when:
            def bucket = Bucket.fromEvents(events)

        then:
            bucket.id == bucketId
            bucket.balance == Money.of(50.0)
    }

    def "should throw exception if opened bucket is reopened"() {
        given:
            def bucketId = UUID.randomUUID()
            def events = []
            events.add(BucketCreatedEvent.createEvent(bucketId))
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(5.0)))
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(15.0)))
            events.add(MoneyTransferredEvent.createEvent(bucketId, Money.of(60.0)))
            events.add(BucketCreatedEvent.createEvent(bucketId))

        when:
            Bucket.fromEvents(events)

        then:
           thrown BucketReopenedException
    }
}
