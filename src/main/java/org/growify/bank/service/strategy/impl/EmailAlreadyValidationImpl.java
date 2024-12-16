package org.growify.bank.service.strategy.impl;

import org.growify.bank.repository.UserRepository;
import org.growify.bank.service.strategy.EmailAlreadyValidationStrategy;

public class EmailAlreadyValidationImpl implements EmailAlreadyValidationStrategy {
    private UserRepository userRepository;

    @Override
    public boolean isEmailAlreadyUsed(String email) {
        return false;
    }

    public void validate(String existingEmail, String newEmail, String userIdToExclude){

    }

    public void checkEmailExists (String email, String userIdToExclude){

    }

}
