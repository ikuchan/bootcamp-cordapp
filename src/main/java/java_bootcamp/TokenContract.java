package java_bootcamp;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

import static net.corda.core.contracts.ContractsDSL.requireThat;

/* Our contract, governing how our state will evolve over time.
 * See src/main/java/examples/ArtContract.java for an example. */
public class TokenContract implements Contract{
    public static String ID = "java_bootcamp.TokenContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        //shape
        if(tx.getOutputStates().size() != 1)
            throw new IllegalArgumentException("tx must have one output");
        if(tx.getInputStates().size() != 0)
            throw new IllegalArgumentException("tx must have no inputs");
        ContractState outputState = tx.getOutput(0);

        if(tx.getCommands().size() != 1)
            throw new IllegalArgumentException("tx must have one command");

        Command command = tx.getCommand(0) ;
        CommandData commandData = command.getValue();
        List<PublicKey> txSigner = command.getSigners();


        if(commandData instanceof Commands.Issue){
            //content
            if(!(outputState instanceof TokenState))
                throw new IllegalArgumentException("output state must be TokenState");
            TokenState outputTokenState = (TokenState) outputState;

            if(!(outputTokenState.getAmount()>0))
                throw new IllegalArgumentException("output state must have positive ammounts");

            //signer
            if(!(txSigner.contains(outputTokenState.getIssuer().getOwningKey())))
                throw new IllegalArgumentException("txSigner must include issuer");
//            if(!(txSigner.contains(outputTokenState.getOwner().getOwningKey())))
//                throw new IllegalArgumentException("txSigner must include owner");
        }else{
            throw new IllegalArgumentException("unrecognized command");
        }
    }

    public interface Commands extends CommandData {
        class Issue implements Commands { }
    }
}