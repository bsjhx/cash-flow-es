package com.bsjhx.cashflow.application.tracksheet.command

import com.bsjhx.cashflow.adapters.outbound.EventStore
import com.bsjhx.cashflow.domain.tracksheet.event.TrackSheetCreatedEvent
import com.bsjhx.cashflow.domain.tracksheet.exception.TrackSheetExceptionReasons
import com.bsjhx.cashflow.domain.tracksheet.exception.TrackSheetMutationException
import org.spockframework.spring.SpringSpy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

import static TrackSheetCommands.*

@SpringBootTest
class OpenTrackSheetCommandHandlerSpec extends Specification {

    @Subject
    @Autowired
    private CreateTrackSheetCommandHandler commandHandler

    @SpringSpy
    private EventStore eventStore

    def "should create new track sheet"() {
        given:
            def trackSheetId = UUID.randomUUID()
            def command = new OpenTrackSheetCommand(trackSheetId)

        when:
            commandHandler.handle(command)

        then:
            1 * eventStore.loadEvents(trackSheetId)
            1 * eventStore.saveEvents(trackSheetId, _)

            def eventsMaybe = eventStore.loadEvents(trackSheetId)
            eventsMaybe.isPresent()
            def events = eventsMaybe.get()
            events.size() == 1
            def event = (TrackSheetCreatedEvent) events.getFirst()
            event.trackSheetId == trackSheetId
    }

    def "should thrown exception trying to create track sheet with same id"() {
        given:
            def trackSheetId = UUID.randomUUID()
            def command = new OpenTrackSheetCommand(trackSheetId)

        when:
            commandHandler.handle(command)
            commandHandler.handle(command)

        then:
            def exception = thrown(TrackSheetMutationException)
            exception.message == TrackSheetExceptionReasons.TRACK_SHEET_ALREADY_OPENED

            2 * eventStore.loadEvents(trackSheetId)
            1 * eventStore.saveEvents(trackSheetId, _)

            eventStore.loadEvents(trackSheetId).isPresent()
            def eventsMaybe = eventStore.loadEvents(trackSheetId)
            def events = eventsMaybe.get()
            events.size() == 1
            def event = (TrackSheetCreatedEvent) events.getFirst()
            event.trackSheetId == trackSheetId
    }
}
