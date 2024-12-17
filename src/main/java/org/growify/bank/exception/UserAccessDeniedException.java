package org.growify.bank.exception;

public class UserAccessDeniedException extends RuntimeException{
    public UserAccessDeniedException() {
        super("User does not have the required permissions.");
    }
}
