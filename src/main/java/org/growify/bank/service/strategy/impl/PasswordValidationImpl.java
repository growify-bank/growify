package org.growify.bank.service.strategy.impl;

import org.growify.bank.exception.PasswordsNotMatchException;
import org.growify.bank.service.strategy.interfaces.PasswordValidationStrategy;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidationImpl implements PasswordValidationStrategy {

    @Override
    public void validate(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordsNotMatchException();
        }
    }
}
