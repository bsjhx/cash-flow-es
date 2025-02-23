package com.bsjhx.cashflow.application.tracksheet.query

import com.bsjhx.cashflow.adapters.outbound.EventStore
import com.bsjhx.cashflow.domain.tracksheet.Money
import com.bsjhx.cashflow.domain.tracksheet.event.MoneyTransferredEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
class CurrentBalanceQueryHandlerTest extends Specification {
    
    @Autowired
    @Subject
    private CurrentBalanceQueryHandler balanceQueryHandler
    
    @Autowired
    private EventStore eventStore
    
    def "should return calculated balance"() {
        given:
            def trackSheetId = UUID.randomUUID()            
            eventStore.saveEvents(trackSheetId, [
                    MoneyTransferredEvent.createEvent(trackSheetId, Money.of(50.0)),
                    MoneyTransferredEvent.createEvent(trackSheetId, Money.of(-25.0)),
                    MoneyTransferredEvent.createEvent(trackSheetId, Money.of(100.0)),
                    MoneyTransferredEvent.createEvent(trackSheetId, Money.of(75.0)),
            ])
        def query = new CurrentBalanceQuery(trackSheetId)
        
        when:
            def actual = balanceQueryHandler.getCurrentBalance(query)
        
        then:
            actual == Money.of(200.0)
    }
}
