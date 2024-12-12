package org.growify.bank.service.strategy;

import org.springframework.security.core.Authentication;

public interface AuthenticateValidationStrategy {
    void validate(Authentication authentication);
}


