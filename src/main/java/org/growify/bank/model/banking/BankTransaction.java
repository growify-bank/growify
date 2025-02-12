package org.growify.bank.model.banking;

import java.sql.Timestamp;

public class BankTransaction {
    private int id;
    public BankAccount accountOrigin;
    private int accountOriginId;
    public BankAccount accountDestination;
    private int accountDestinationId;
    public int amount;
    private Timestamp transferDate;
}
