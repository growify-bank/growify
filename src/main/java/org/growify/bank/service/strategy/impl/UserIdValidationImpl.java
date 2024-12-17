package org.growify.bank.service.strategy.impl;

import org.growify.bank.exception.UserAccessDeniedException;
import org.growify.bank.model.user.User;
import org.growify.bank.service.strategy.interfaces.UserIdValidationStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Component
public class UserIdValidationImpl implements UserIdValidationStrategy {

    @Override
    public void validateUserId(Authentication authentication, String userId) {
        String authenticatedUserId = ((User) authentication.getPrincipal()).getId();
        if(!userId.equals(authenticatedUserId)) {
            throw new UserAccessDeniedException();
        }
    }
}
