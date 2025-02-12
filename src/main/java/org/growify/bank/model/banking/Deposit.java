package org.growify.bank.model.banking;

public class Deposit extends BankTransaction {

    public Void Execute() {
        if (accountOrigin == null) {
            throw new IllegalArgumentException("Origin account is required for deposit.");
        }

        accountOrigin.balance += amount;
        return null;
    }
}
