package org.growify.bank.service.strategy;

public interface PasswordValidationStrategy {
    boolean validatePassword(String password);
}