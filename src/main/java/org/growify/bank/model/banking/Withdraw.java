package org.growify.bank.model.banking;

public class Withdraw extends BankTransaction{

    public Void Execute(){
        if(accountOrigin.balance >= amount){
            accountOrigin.balance -= amount;
        }else {
            return null;
        }
        return null;
    }
}
