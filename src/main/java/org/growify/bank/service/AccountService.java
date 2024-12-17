package org.growify.bank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.growify.bank.dto.request.LoginRequestDTO;
import org.growify.bank.dto.request.RefreshTokenRequestDTO;
import org.growify.bank.dto.request.RegisterRequestDTO;
import org.growify.bank.dto.response.TokenResponseDTO;
import org.growify.bank.exception.EmailAlreadyExistsException;
import org.growify.bank.exception.UserEmailNotFoundException;
import org.growify.bank.infrastructure.security.TokenService;
import org.growify.bank.model.user.User;
import org.growify.bank.model.user.UserRole;
import org.growify.bank.repository.UserRepository;
import org.growify.bank.service.strategy.impl.AccountLoginManagerImpl;
import org.growify.bank.service.strategy.impl.PasswordValidationImpl;
import org.growify.bank.service.strategy.impl.TokenManagerImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.security.auth.login.AccountLockedException;
import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AccountLoginManagerImpl accountLoginManager;
    private final TokenManagerImpl tokenManagerImpl;
    private final PasswordValidationImpl passwordValidationImpl;
    private final TokenService tokenService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public ResponseEntity<TokenResponseDTO> loginAccount(LoginRequestDTO authDto, AuthenticationManager authManager) throws AccountLockedException {

        try {
            var authResult = accountLoginManager.authenticateUser(authDto, authManager);
            var user = (User) authResult.getPrincipal();

            accountLoginManager.handleSuccessfulLogin(user);
            tokenManagerImpl.revokeAllUserTokens(user);

            log.info("[USER_AUTHENTICATED] User: {} successfully authenticated", user.getEmail());
            return ResponseEntity.ok(tokenManagerImpl.generateTokenResponse(user));
        } catch (LockedException e) {
            log.warn("[USER_LOCKED] User: {} is locked", authDto.email());
            throw new AccountLockedException();
        } catch (BadCredentialsException e) {
            log.warn("[USER_LOGIN_FAILED] User: {} failed to authenticate", authDto.email());
            return accountLoginManager.handleBadCredentials(authDto.email());
        }
    }

    public ResponseEntity<TokenResponseDTO> registerAccount(RegisterRequestDTO registerRequestDTO) {

        if (isEmailExists(registerRequestDTO.email())) {
            log.warn("[USER_EXISTS] User: {} already exists", registerRequestDTO.email());
            throw new EmailAlreadyExistsException();
        }

        passwordValidationImpl.validate(registerRequestDTO.password(), registerRequestDTO.confirmPassword());

        User newUser = buildUserFromRegistration(registerRequestDTO);
        User savedUser = userRepository.save(newUser);
        String baseUri = "http://localhost:8080";

        URI uri = UriComponentsBuilder.fromUriString(baseUri)
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        log.info("[USER_REGISTERED] User: {} successfully registered", savedUser.getEmail());
        return ResponseEntity.created(uri).body(tokenManagerImpl.generateTokenResponse(savedUser));
    }

    public ResponseEntity<TokenResponseDTO> refreshToken(RefreshTokenRequestDTO tokenRequest) {

        String userEmail = tokenService.validateToken(tokenRequest.refreshToken());

        if (userEmail == null || userEmail.isEmpty()) {
            log.error("[TOKEN_REFRESH_FAILED] Invalid user email found in token.");
            throw new UserEmailNotFoundException();
        }

        UserDetails userDetails = loadUserByUsername(userEmail);

        var user = (User) userDetails;
        tokenManagerImpl.revokeAllUserTokens(user);

        log.info("[TOKEN_REFRESHED] User: {} successfully refreshed their token.", user.getEmail());
        return ResponseEntity.ok(tokenManagerImpl.generateTokenResponse(user));
    }

    private boolean isEmailExists(String email) {
        return loadUserByUsername(email) != null;
    }

    private User buildUserFromRegistration(RegisterRequestDTO registerRequestDTO) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerRequestDTO.password());
        return new User(
                registerRequestDTO.name(),
                registerRequestDTO.email(),
                encryptedPassword,
                true,
                UserRole.USER
        );
    }
}
