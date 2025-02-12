package org.growify.bank.model.banking;

import org.growify.bank.model.enums.AccountType;
import org.growify.bank.model.user.User;

public class BankAccount {

    private int id;
    private int accountNumber;
    public double balance;
    private int agency;
    private AccountType accountType;
    private User user;
}
