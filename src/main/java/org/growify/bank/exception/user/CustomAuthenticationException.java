package org.growify.bank.exception.user;

public class CustomAuthenticationException extends RuntimeException {
    public CustomAuthenticationException() {
        super("Invalid username or password");
    }
}
