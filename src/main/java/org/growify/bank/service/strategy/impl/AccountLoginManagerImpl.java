package org.growify.bank.service.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.growify.bank.dto.request.LoginRequestDTO;
import org.growify.bank.dto.response.TokenResponseDTO;
import org.growify.bank.exception.CustomAuthenticationException;
import org.growify.bank.model.user.User;
import org.growify.bank.repository.UserRepository;
import org.growify.bank.service.strategy.interfaces.AccountLoginManagerStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountLoginManagerImpl implements AccountLoginManagerStrategy {

    private final UserRepository userRepository;

    @Override
    public Authentication authenticateUser(LoginRequestDTO authDto, AuthenticationManager authManager) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());
        return authManager.authenticate(usernamePassword);
    }

    @Override
    public void handleSuccessfulLogin(User user) {
        if (!user.isEnable()) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            userRepository.save(user);
            if (user.getFailedLoginAttempts() >= 4) {
                user.lockAccountForHours();
                userRepository.save(user);
            }
            throw new LockedException("User account is locked");
        }
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }

    @Override
    public ResponseEntity<TokenResponseDTO> handleBadCredentials(String email) {
        var user = (User) userRepository.findByEmail(email);
        if (user != null) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            userRepository.save(user);
            if (user.getFailedLoginAttempts() >= 4) {
                user.lockAccountForHours();
                userRepository.save(user);
            }
        }
        throw new CustomAuthenticationException();
    }
}
