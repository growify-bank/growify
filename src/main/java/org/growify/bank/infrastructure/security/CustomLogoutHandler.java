package org.growify.bank.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.growify.bank.exception.TokenGenerationException;
import org.growify.bank.model.token.Token;
import org.growify.bank.repository.TokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Slf4j
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final SecurityFilter securityFilter;
    private final TokenRepository tokenRepository;
    private static final String TOKEN_EXPIRED_MESSAGE = "Token is expired";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = securityFilter.recoverTokenFromRequest(request);

        Token storedToken = findTokenByValue(token);

        if (storedToken != null) {
            invalidateToken(storedToken);
            SecurityContextHolder.clearContext();
            log.info("[LOGOUT_SUCCESS] User logged out successfully. Token invalidated: {}", token);
        } else {
            log.error("[TOKEN_EXPIRED] Token not found during logout: {}", token);
            try {
                sendUnauthorizedResponse(response);
            } catch (IOException e) {
                log.error("[LOGOUT_ERROR] Error sending unauthorized response", e);
                throw new TokenGenerationException("Error sending unauthorized response", e);
            }
        }
    }

    private Token findTokenByValue(String tokenValue) {
        Token token = tokenRepository.findByTokenValue(tokenValue).orElse(null);
        if (token == null) {
            log.warn("[TOKEN_EXPIRED] Token not found in repository during logout for value: {}", tokenValue);
        }
        return token;
    }

    private void invalidateToken(Token token) {
        token.setTokenExpired(true);
        token.setTokenRevoked(true);
        tokenRepository.save(token);
    }

    public void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(TOKEN_EXPIRED_MESSAGE);
    }
}
