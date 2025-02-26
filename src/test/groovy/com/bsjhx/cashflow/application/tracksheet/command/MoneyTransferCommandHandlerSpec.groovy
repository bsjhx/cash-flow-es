package com.bsjhx.cashflow.application.tracksheet.command

import com.bsjhx.cashflow.adapters.outbound.EventStore
import com.bsjhx.cashflow.domain.tracksheet.TrackSheet
import com.bsjhx.cashflow.domain.tracksheet.Money
import org.spockframework.spring.SpringSpy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

import static TrackSheetCommands.*

@SpringBootTest
class MoneyTransferCommandHandlerSpec extends Specification {

    @Subject
    @Autowired
    private MoneyTransferCommandHandler commandHandler

    @Autowired
    private CreateTrackSheetCommandHandler openTrackSheetCommandHandler

    @SpringSpy
    private EventStore eventStore

    def "should transfer money"() {
        given:
            def trackSheetId = UUID.randomUUID()
            def openTrackSheetCommand = new OpenTrackSheetCommand(trackSheetId)
            openTrackSheetCommandHandler.handle(openTrackSheetCommand)

        when:
            commandHandler.handle(new TransferMoneyCommand(trackSheetId, 5.0))
            commandHandler.handle(new TransferMoneyCommand(trackSheetId, 15.0))
            commandHandler.handle(new TransferMoneyCommand(trackSheetId, 70.0))
            commandHandler.handle(new TransferMoneyCommand(trackSheetId, -50.0))
        
        then:
            4 * eventStore.loadEvents(trackSheetId)
            4 * eventStore.saveEvents(trackSheetId, _)

            def eventsMaybe = eventStore.loadEvents(trackSheetId)
            eventsMaybe.isPresent()
            def events = eventsMaybe.get()
            events.size() == 5
            def actual = TrackSheet.fromEvents(events)
            Money.of(5.0 + 15.0 + 70.0 - 50.0) == actual.balance
    }

    def "should throw exception for not existing track sheet"() {
        given:
            def trackSheetId = UUID.randomUUID()

        when:
            commandHandler.handle(new TransferMoneyCommand(trackSheetId, 5.0))

        then:
            def exception = thrown(IllegalArgumentException)
            exception.message == "Track sheet not found"
    }
}
