package org.growify.bank.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.growify.bank.exception.user.TokenInvalidException;
import org.growify.bank.repository.TokenRepository;
import org.growify.bank.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    final TokenService tokenService;
    final TokenRepository tokenRepository;
    final UserRepository userRepository;
    final HttpServletRequest request;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = recoverTokenFromRequest(request);
            if (token != null && !handleAuthentication(token, response)) {
                return;
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("[ERROR_FILTER] Error processing security filter: {}", e.getMessage());
            throw e;
        }
    }

    public String recoverTokenFromRequest(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            log.debug("[NO_AUTH_HEADER] No Authorization header found in the request.");
            return null;
        }
        String token = authHeader.replace("Bearer ", "");
        log.debug("[RECOVERED] Token recovered from Authorization header: {}", token);
        return token;
    }

    protected boolean handleAuthentication(String token, HttpServletResponse response)
            throws IOException {

        try {
            String email = tokenService.validateToken(token);
            UserDetails userDetails = userRepository.findByEmail(email);
            boolean isTokenValid = isTokenValid(token);

            if (userDetails != null && isTokenValid) {
                setAuthenticationInSecurityContext(userDetails);
                log.info("[USER_AUTHENTICATED] User: {} successfully authenticated with token: {}.", userDetails.getUsername(), token);
                return true;
            } else {
                getError(token);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
        } catch (TokenInvalidException e) {
            handleInvalidToken(response, e);
            getError(token);
            return false;
        }
    }

    private void getError(String token) {
        log.error("[TOKEN_FAILED] User-Agent: {}. IP Address: {}. Validation failed for token: {}",
                getUserAgent(), getIpAddress(), token);
    }

    private String getIpAddress() {
        return request.getRemoteAddr();
    }

    private String getUserAgent() {
        return request.getHeader("User-Agent");
    }

    private UserDetails getUserDetailsByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean isTokenValid(String token) {
        return tokenRepository.findByTokenValue(token)
                .map(t -> !t.isTokenExpired() && !t.isTokenRevoked())
                .orElse(false);
    }

    public void setAuthenticationInSecurityContext(UserDetails userDetails) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void handleInvalidToken(HttpServletResponse response, TokenInvalidException e)
            throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(e.getMessage());
        log.warn("[TOKEN_INVALID] Invalid token detected: {}", e.getMessage());
    }
}
