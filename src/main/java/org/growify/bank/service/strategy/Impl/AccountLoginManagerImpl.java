package org.growify.bank.service.strategy.Impl;

import org.growify.bank.dto.request.LoginRequestDTO;
import org.growify.bank.dto.response.TokenResponseDTO;
import org.growify.bank.model.user.User;
import org.growify.bank.repository.UserRepository;
import org.growify.bank.service.strategy.AccountLoginManagerStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

public class AccountLoginManagerImpl implements AccountLoginManagerStrategy {
    private UserRepository userRepository;

    @Override
    public Authentication authenticateUser(LoginRequestDTO authDto, AuthenticationManager authManager) {
        return null;
    }

    @Override
    public void handleSuccessfulLogin(User user) {

    }

    @Override
    public ResponseEntity<TokenResponseDTO> handleBadCredentials(String email) {
        return null;
    }
}
