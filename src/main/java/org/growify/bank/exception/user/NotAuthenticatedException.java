package org.growify.bank.exception;

public class NotAuthenticatedException extends RuntimeException {
    public NotAuthenticatedException(){
        super("User isn't authenticated.");
    }
}
