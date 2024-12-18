package org.growify.bank.exception.user;

public class NotAuthenticatedException extends RuntimeException {
    public NotAuthenticatedException(){
        super("User isn't authenticated.");
    }
}
