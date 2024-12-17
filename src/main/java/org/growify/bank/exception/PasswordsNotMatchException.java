package org.growify.bank.exception;

public class PasswordsNotMatchException extends RuntimeException {
    public PasswordsNotMatchException() {
        super("Passwords do not match");
    }
}
