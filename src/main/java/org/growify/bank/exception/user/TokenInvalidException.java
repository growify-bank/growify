package org.growify.bank.exception.user;

public class TokenInvalidException extends RuntimeException{

    public TokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
