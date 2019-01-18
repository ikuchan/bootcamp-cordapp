package java_bootcamp;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

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


    }

    public interface Commands extends CommandData {
        class Issue implements Commands { }
    }
}