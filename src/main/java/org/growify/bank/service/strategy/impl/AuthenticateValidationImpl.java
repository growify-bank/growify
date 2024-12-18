package org.growify.bank.service.strategy.impl;

import org.growify.bank.exception.user.NotAuthenticatedException;
import org.growify.bank.service.strategy.interfaces.AuthenticateValidationStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateValidationImpl implements AuthenticateValidationStrategy {

    @Override
    public void validate(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated())
            throw new NotAuthenticatedException();
    }
}
