package org.growify.bank.service.strategy;

import org.growify.bank.dto.request.LoginRequestDTO;
import org.growify.bank.dto.request.RefreshTokenDTO;
import org.growify.bank.dto.request.RegisterRequestDTO;
import org.growify.bank.dto.response.TokenResponseDTO;
import org.growify.bank.infrastructure.security.TokenService;
import org.growify.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordValidationStrategy passwordValidationStrategy;

    public ResponseEntity<TokenResponseDTO> loginAccount(LoginRequestDTO authDto) {
        return null;
    }

    public ResponseEntity<TokenResponseDTO> registerAccount(RegisterRequestDTO registerRequestDTO) {
        return null;
    }

    public ResponseEntity<TokenResponseDTO> refreshToken(RefreshTokenDTO refreshTokenRequestDTO) {
        return null;
    }
}