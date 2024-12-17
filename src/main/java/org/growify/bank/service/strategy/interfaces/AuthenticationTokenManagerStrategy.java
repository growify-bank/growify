package org.growify.bank.service.strategy;

import org.growify.bank.dto.response.TokenResponseDTO;
import org.growify.bank.model.user.User;

public interface AuthenticationTokenManagerStrategy {
    TokenResponseDTO generateTokenResponse(User user);
    void revokeAllUserTokens(User user);
}