package java_bootcamp;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.Party;
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

            //Signer
            Party owner = houseState.getOwner();
            PublicKey ownersKey = owner.getOwningKey();
            if(!(requiredSigners.contains(ownersKey)))
                throw new IllegalArgumentException("owner must sign.");



        }else if(commandType instanceof Transfer){
            //shape
            if(tx.getInputStates().size() != 1)
                throw new IllegalArgumentException("Input must have one state");
            if(tx.getOutputStates().size() != 1)
                throw new IllegalArgumentException("Output must have one state");
            ContractState inputState = tx.getInput(0);
            ContractState outputState = tx.getOutput(0);

            if(!(inputState instanceof HouseState))
                throw new IllegalArgumentException("Input must be a HouseState");
            if(!(outputState instanceof HouseState))
                throw new IllegalArgumentException("Output must be a HouseState");

            //Content
            HouseState inputHouseState = (HouseState)inputState;
            HouseState outputHouseState = (HouseState)outputState;

            if(!(inputHouseState.getAddress().equals(outputHouseState.getAddress())))
                throw new IllegalArgumentException("In a transfer, the address can't change");

            if(!(outputHouseState.getOwner().equals(outputHouseState.getOwner())))
                throw new IllegalArgumentException("In a transfer, the owner must change");

            //signer
            Party inputOwner = inputHouseState.getOwner();
            Party outputOwner = outputHouseState.getOwner();

            if(!(requiredSigners.contains(inputOwner.getOwningKey())))
                throw new IllegalArgumentException("Current owner must sign transfer.");
            if(!(requiredSigners.contains(outputOwner.getOwningKey())))
                throw new IllegalArgumentException("Current owner must sign transfer.");

        }else {
            throw new IllegalArgumentException("Command type is illegal");
        }
   }

    public class Register implements CommandData{}
    public class Transfer implements CommandData{}
}


