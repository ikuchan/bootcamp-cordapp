package java_bootcamp;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.identity.Party;

public class TwoPartyFlow extends FlowLogic<Integer> {
    private Party counterParty;
    private Integer number;

    public TwoPartyFlow(Party counterParty, Integer number) {
        this.counterParty = counterParty;
        this.number = number;
    }

    @Suspendable
    public Integer call() throws FlowException {
        FlowSession session = initiateFlow(counterParty);
        session.send(number);

        int receivedIncrementedInteger  = session.receive(Integer.class).unwrap(it->it);

        return receivedIncrementedInteger;


    }
}
