package org.growify.bank.exception;

public class CustomAuthenticationException extends RuntimeException {
    public CustomAuthenticationException() {
        super("Invalid username or password");
    }
}
