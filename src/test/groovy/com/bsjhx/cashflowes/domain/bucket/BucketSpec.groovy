package com.bsjhx.cashflowes.domain.bucket

import com.bsjhx.cash_flow_es.domain.event.BucketCreatedEvent
import spock.lang.Specification

class BucketSpec extends Specification {

    def "jsodifhisdu"() {
        given:
            def event = BucketCreatedEvent.createEvent()
        
        when:
            def bucket = Bucket.fromEvents([event])
            
        then:
            bucket.balance == Money.of(0.0)
    }
}
