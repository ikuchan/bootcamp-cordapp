package java_bootcamp;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.StateAndContract;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatedBy;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.node.NodeInfo;
import net.corda.core.node.ServiceHub;

import java.util.List;

@InitiatedBy(TwoPartyFlow.class)
public class TwoPartyFlowResponder extends FlowLogic<Void> {
    private FlowSession counterpartySession;
    public TwoPartyFlowResponder(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    public Void call() throws FlowException {
        ServiceHub serviceHub = getServiceHub();

        //データの取得
        List<StateAndRef<HouseState>> statesFromVault =
            serviceHub.getVaultService().queryBy(HouseState.class).getStates();

        //Cordaノードの取得
        CordaX500Name alicesName = new CordaX500Name("Alice","Manchester","UK");
        serviceHub.getNetworkMapCache().getNodeByLegalName(alicesName);

        //バージョン情報
        int platformVersion = serviceHub.getMyInfo().getPlatformVersion();

        int receiveInt = counterpartySession.receive(Integer.class).unwrap(it ->{
            if(it >3)throw new IllegalArgumentException("Number too high.");
            return it;
        });

        int receivedIntPlusOne = receiveInt + 1;

        counterpartySession.send(receivedIntPlusOne);

        return null;
    }
}
