package org.growify.bank.service.strategy;

public interface EmailAlreadyValidationStrategy {
    boolean isEmailAlreadyUsed(String email);
}