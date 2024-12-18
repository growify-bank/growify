package org.growify.bank.exception.user;

public class UserEmailNotFoundException extends RuntimeException{
    public UserEmailNotFoundException() {
        super("User email not found in token.");
    }
}
