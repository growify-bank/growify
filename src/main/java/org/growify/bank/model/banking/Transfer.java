package org.growify.bank.model.banking;

public class Transfer extends BankTransaction {

    public Void Execute (){
        if(accountOrigin.balance >= amount){
            accountOrigin.balance -= amount;
            accountDestination.balance += amount;
        }else{
            return null;
        }
        return null;
    }
}
