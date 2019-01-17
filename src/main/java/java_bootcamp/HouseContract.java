package java_bootcamp;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

public class HouseContract implements Contract {
    public HouseContract() {
    }

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        if (tx.getCommands().size() != 1)
            throw new IllegalArgumentException("Transaction must have one command");
        Command command = tx.getCommand(0);
        List<PublicKey> requiredSigners = command.getSigners();
        CommandData commandType = command.getValue();
        if (commandType instanceof Register){
            //Shape
            if(tx.getInputStates().size() != 0)
                throw new IllegalArgumentException("Regist tx must have no inputs.");
            if(tx.getOutputStates().size() != 1)
                throw new IllegalArgumentException("Regist tx must have one onput.");


            //Content
            ContractState outputState = tx.getOutput(0);
            if(!(outputState instanceof HouseState))
                throw new IllegalArgumentException("output state must be a HouseState");
            HouseState houseState = (HouseState)outputState;
            if (houseState.getAddress().length() <= 3)
                throw new IllegalArgumentException("Address must be longer than 3 characters.");
            if (houseState.getOwner().getName().getCountry().equals("Brazil"))
                throw new IllegalArgumentException("owner must be Brazil");

            //TODO("Signer")


        }else if(commandType instanceof Transfer){
            //TODO("transfer logic")
        }else {
            throw new IllegalArgumentException("Command type is illegal");
        }
   }

    public class Register implements CommandData{}
    public class Transfer implements CommandData{}
}


