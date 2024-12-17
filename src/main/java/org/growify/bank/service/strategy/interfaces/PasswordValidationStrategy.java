package org.growify.bank.service.strategy.interfaces;

public interface PasswordValidationStrategy {
    void validate(String password, String confirmPassword);
}