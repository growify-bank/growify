package org.growify.bank.service.strategy.interfaces;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.growify.bank.dto.request.LoginRequestDTO;
import org.growify.bank.dto.response.TokenResponseDTO;
import org.growify.bank.model.user.User;

public interface AccountLoginManagerStrategy {
    Authentication authenticateUser(LoginRequestDTO authDto, AuthenticationManager authManager);
    void handleSuccessfulLogin(User user);
    ResponseEntity<TokenResponseDTO> handleBadCredentials(String email);
}
