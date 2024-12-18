package org.growify.bank.exception.user;

public class InvalidOldPasswordException extends RuntimeException {
    public InvalidOldPasswordException() {
        super("The old password provided is incorrect.");
    }
}
