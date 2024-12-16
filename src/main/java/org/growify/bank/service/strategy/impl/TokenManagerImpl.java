package org.growify.bank.service.strategy.impl;

import org.growify.bank.dto.response.TokenResponseDTO;
import org.growify.bank.infrastructure.security.TokenService;
import org.growify.bank.model.token.Token;
import org.growify.bank.model.user.User;
import org.growify.bank.repository.TokenRepository;
import org.growify.bank.repository.UserRepository;
import org.growify.bank.service.strategy.AuthenticationTokenManagerStrategy;

public class TokenManagerImpl implements AuthenticationTokenManagerStrategy {
    private UserRepository userRepository;
    private TokenService tokenService;
    private TokenRepository tokenRepository;

    @Override
    public TokenResponseDTO generateTokenResponse(User user) {
        return null;
    }

    @Override
    public void revokeAllUserTokens(User user) {

    }

    public void clearTokens(String userId){

    }

    public Token saveUserToken(User user, String jwtToken){
        return null;
    }
}
