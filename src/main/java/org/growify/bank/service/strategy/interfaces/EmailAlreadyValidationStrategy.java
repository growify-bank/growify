package org.growify.bank.service.strategy.interfaces;

public interface EmailAlreadyValidationStrategy {
    void validate(String existingEmail, String newEmail, String userIdToExclude);
}