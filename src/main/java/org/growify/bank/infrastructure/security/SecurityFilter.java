package org.growify.bank.infrastructure.security;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.growify.bank.exception.TokenInvalidException;
import org.growify.bank.model.user.User;
import org.growify.bank.repository.TokenRepository;
import org.growify.bank.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    final TokenService tokenService;
    final TokenRepository tokenRepository;
    final UserRepository userRepository;
    final UserDetailsService userDetailsService; // Adicionado
    final HttpServletRequest request;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = recoverTokenFromRequest(request);
            if (token != null) {
                handleAuthentication(token, response);
            }

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            log.error("[ERROR_FILTER] Error processing security filter: {}", ex.getMessage());
            throw ex;
        }
    }

    public String recoverTokenFromRequest(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            log.debug("[NO_AUTH_HEADER] No Authorization header found in the request.");
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }

    public void handleAuthentication(String token, HttpServletResponse response) throws IOException {
        try {
            String email = tokenService.validateToken(token);
            UserDetails userDetails = getUserDetailsByEmail(email);
            boolean isTokenValid = isTokenValid(token);

            if (userDetails != null && isTokenValid) {
                setAuthenticationInSecurityContext(userDetails);
            } else {
                getError(token);
            }
        } catch (TokenInvalidException e) {
            handleInvalidToken(response, e);
            getError(token);
        }
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

    public void handleInvalidToken(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(e.getMessage());
        log.warn("[TOKEN_INVALID] Invalid token detected: {}", e.getMessage());
    }

    private UserDetails getUserDetailsByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(user -> userDetailsService.loadUserByUsername(user.getEmail())).orElse(null);
    }

    private void getError(String token) {
        log.error("[TOKEN_FAILED] User-Agent: {}. IP Address: {}. Validation failed for token: {}",
                getUserAgent(), getIpAddress(), token);
    }

    private String getUserAgent() {
        return request.getHeader("User-Agent");
    }

    private String getIpAddress() {
        return request.getRemoteAddr();
    }
}
