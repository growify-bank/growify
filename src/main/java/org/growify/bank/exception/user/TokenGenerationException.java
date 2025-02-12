package org.growify.bank.exception.user;

public class TokenGenerationException extends RuntimeException {
    
    public TokenGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
