package org.growify.bank.service.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.growify.bank.dto.response.TokenResponseDTO;
import org.growify.bank.infrastructure.security.TokenService;
import org.growify.bank.model.token.Token;
import org.growify.bank.model.token.TokenType;
import org.growify.bank.model.user.User;
import org.growify.bank.repository.TokenRepository;
import org.growify.bank.repository.UserRepository;
import org.growify.bank.service.strategy.interfaces.AuthenticationTokenManagerStrategy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenManagerImpl implements AuthenticationTokenManagerStrategy {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;

    @Override
    public TokenResponseDTO generateTokenResponse(User user) {
        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        clearTokens(user.getId());
        Token accessTokenValue = saveUserToken(user, accessToken);
        Token refreshTokenValue = saveUserToken(user, refreshToken);

        user.getTokens().clear();
        user.getTokens().add(accessTokenValue);
        user.getTokens().add(refreshTokenValue);
        userRepository.save(user);

        return new TokenResponseDTO(accessTokenValue.getTokenValue(), refreshTokenValue.getTokenValue());
    }

    @Override
    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user);
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setTokenExpired(true);
                token.setTokenRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }

    public void clearTokens(String userId) {
        var tokens = tokenRepository.findAllByUserId(userId);
        if (!tokens.isEmpty()) {
            tokenRepository.deleteAll(tokens);
        }
    }

    public Token saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .tokenValue(jwtToken)
                .tokenType(TokenType.BEARER)
                .tokenExpired(false)
                .tokenRevoked(false)
                .build();

        return tokenRepository.save(token);
    }
}
