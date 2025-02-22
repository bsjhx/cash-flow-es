package com.bsjhx.cashflow.application.bucket.command

import com.bsjhx.cashflow.adapters.outbound.EventStore
import com.bsjhx.cashflow.domain.tracksheet.event.BucketCreatedEvent
import com.bsjhx.cashflow.domain.tracksheet.exception.BucketExceptionReasons
import com.bsjhx.cashflow.domain.tracksheet.exception.BucketMutationException
import org.spockframework.spring.SpringSpy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

import static com.bsjhx.cashflow.application.bucket.command.BucketCommands.*

@SpringBootTest
class OpenBucketCommandHandlerSpec extends Specification {

    @Subject
    @Autowired
    private OpenBucketCommandHandler commandHandler

    @SpringSpy
    private EventStore eventStore

    def "should create new bucket "() {
        given:
            def bucketId = UUID.randomUUID()
            def command = new OpenBucketCommand(bucketId)

        when:
            commandHandler.handle(command)

        then:
            1 * eventStore.loadEvents(bucketId)
            1 * eventStore.saveEvents(bucketId, _)

            def eventsMaybe = eventStore.loadEvents(bucketId)
            eventsMaybe.isPresent()
            def events = eventsMaybe.get()
            events.size() == 1
            def event = (BucketCreatedEvent) events.getFirst()
            event.bucketId == bucketId
    }

    def "should thrown exception trying to create bucket with same id"() {
        given:
            def bucketId = UUID.randomUUID()
            def command = new OpenBucketCommand(bucketId)

        when:
            commandHandler.handle(command)
            commandHandler.handle(command)

        then:
            def exception = thrown(BucketMutationException)
            exception.message == BucketExceptionReasons.BUCKET_ALREADY_OPENED

            2 * eventStore.loadEvents(bucketId)
            1 * eventStore.saveEvents(bucketId, _)

            eventStore.loadEvents(bucketId).isPresent()
            def eventsMaybe = eventStore.loadEvents(bucketId)
            def events = eventsMaybe.get()
            events.size() == 1
            def event = (BucketCreatedEvent) events.getFirst()
            event.bucketId == bucketId
    }
}
