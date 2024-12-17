package org.growify.bank.service.strategy.interfaces;

import org.springframework.security.core.Authentication;

public interface UserIdValidationStrategy {
    void validateUserId(Authentication authentication, String userId);
}
