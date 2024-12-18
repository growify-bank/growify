package org.growify.bank.exception.user;

public class PasswordsNotMatchException extends RuntimeException {
    public PasswordsNotMatchException() {
        super("Passwords do not match");
    }
}
