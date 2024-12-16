package org.growify.bank.service.strategy.impl;

import org.growify.bank.service.strategy.PasswordValidationStrategy;

public class PasswordValidationImpl implements PasswordValidationStrategy {
    @Override
    public boolean validatePassword(String password) {
        return false;
    }

    public void validate(String password, String confirmPassword){

    }
}
