package com.bsjhx.cashflow.application.bucket.command

import com.bsjhx.cashflow.adapters.outbound.EventStore
import com.bsjhx.cashflow.domain.bucket.Bucket
import com.bsjhx.cashflow.domain.bucket.Money
import org.spockframework.spring.SpringSpy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

import static com.bsjhx.cashflow.application.bucket.command.BucketCommands.*

@SpringBootTest
class MoneyTransferCommandHandlerSpec extends Specification {

    @Subject
    @Autowired
    private MoneyTransferCommandHandler commandHandler

    @Autowired
    private OpenBucketCommandHandler openBucketCommandHandler

    @SpringSpy
    private EventStore eventStore

    def "should transfer money"() {
        given:
            def bucketId = UUID.randomUUID()
            def openBucketCommand = new OpenBucketCommand(bucketId)
            openBucketCommandHandler.handle(openBucketCommand)

        when:
            commandHandler.handle(new TransferMoneyCommand(bucketId, 5.0))
            commandHandler.handle(new TransferMoneyCommand(bucketId, 15.0))
            commandHandler.handle(new TransferMoneyCommand(bucketId, 70.0))
            commandHandler.handle(new TransferMoneyCommand(bucketId, -50.0))
        
        then:
            4 * eventStore.loadEvents(bucketId)
            4 * eventStore.saveEvents(bucketId, _)

            def eventsMaybe = eventStore.loadEvents(bucketId)
            eventsMaybe.isPresent()
            def events = eventsMaybe.get()
            events.size() == 5
            def actual = Bucket.fromEvents(events)
            Money.of(5.0 + 15.0 + 70.0 - 50.0) == actual.balance
    }

    def "should throw exception for not existing bucket"() {
        given:
            def bucketId = UUID.randomUUID()

        when:
            commandHandler.handle(new TransferMoneyCommand(bucketId, 5.0))

        then:
            def exception = thrown(IllegalArgumentException)
            exception.message == "Bucket not found"
    }
}
