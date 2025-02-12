package org.growify.bank.service.strategy.interfaces;

import org.springframework.security.core.Authentication;

public interface AuthenticateValidationStrategy {
    void validate(Authentication authentication);
}


