package org.growify.bank.exception.user;

public class UserAccessDeniedException extends RuntimeException{
    public UserAccessDeniedException() {
        super("User does not have the required permissions.");
    }
}
