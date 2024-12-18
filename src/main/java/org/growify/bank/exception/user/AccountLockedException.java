package org.growify.bank.exception.user;

public class AccountLockedException extends RuntimeException {
    public AccountLockedException() {
        super("Account is locked.");
    }
}
