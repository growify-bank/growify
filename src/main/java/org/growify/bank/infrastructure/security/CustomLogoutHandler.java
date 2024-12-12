package org.growify.bank.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.growify.bank.model.token.Token;
import org.growify.bank.repository.TokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final SecurityFilter securityFilter;
    private final TokenRepository tokenRepository;
    private static final String TOKEN_EXPIRED_MESSAGE = "Token is expired";

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {

    }

    private Token findTokenByValue(String tokenVale){
        return new Token();
    }

    private void invalidateToken(Token token){
        
    }
}
