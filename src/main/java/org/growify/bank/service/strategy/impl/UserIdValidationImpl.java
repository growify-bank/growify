package org.growify.bank.service.strategy.impl;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.growify.bank.service.strategy.UserIdValidationStrategy;


public class UserIdValidationImpl implements UserIdValidationStrategy {
    @Override
    public boolean validateUserId(String userId) {
        return false;
    }

    public void validate(Authentication authentication, String UserId){

    }
}
