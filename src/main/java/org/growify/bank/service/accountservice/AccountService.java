package org.growify.bank.service.accountservice;

import org.growify.bank.dto.request.LoginRequestDTO;
import org.growify.bank.dto.request.RegisterRequestDTO;
import org.growify.bank.dto.request.RefreshTokenDTO;
import org.growify.bank.dto.response.TokenResponseDTO;
import org.growify.bank.service.strategy.PasswordValidationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.growify.bank.model.user.User;
import org.growify.bank.repository.UserRepository;
import org.growify.bank.security.JwtTokenProvider;
import org.growify.bank.security.TokenService;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationTokenManager authenticationTokenManager;

    @Autowired
    private AccountLoginManager accountLoginManager;

    @Autowired
    private PasswordValidationStrategy passwordValidationStrategy;

    public UserDetails loadUserByUsername(String username){
        // Implementação aqui
        return null;
    }
    public ResponseEntity<TokenResponseDTO> loginAccount(LoginRequestDTO authDto, AuthenticationManager authManager) {
        // Implementação aqui
        return null;
    }
    public ResponseEntity<TokenResponseDTO> registerAccount(RegisterRequestDTO registerRequestDTO) {
        // Implementação aqui
        return null;
    }
    public ResponseEntity<TokenResponseDTO> refreshToken(RefreshTokenDTO refreshTokenRequestDTO) {
        // Implementação aqui
        return null;
    }
    public User buildUserFromRegistrationDto(RegisterRequestDTO registerRequestDTO) {
        // Implementação aqui
        return null;

    }


}
