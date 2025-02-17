package com.bsjhx.cashflow.application.bucket.query

import com.bsjhx.cashflow.adapters.outbound.EventStore
import com.bsjhx.cashflow.domain.bucket.Money
import com.bsjhx.cashflow.domain.bucket.event.MoneyTransferredEvent
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
            def bucketId = UUID.randomUUID()            
            eventStore.saveEvents(bucketId, [
                    MoneyTransferredEvent.createEvent(bucketId, Money.of(50.0)),
                    MoneyTransferredEvent.createEvent(bucketId, Money.of(-25.0)),
                    MoneyTransferredEvent.createEvent(bucketId, Money.of(100.0)),
                    MoneyTransferredEvent.createEvent(bucketId, Money.of(75.0)),
            ])
        def query = new CurrentBalanceQuery(bucketId)
        
        when:
            def actual = balanceQueryHandler.getCurrentBalance(query)
        
        then:
            actual == Money.of(200.0)
    }
}
