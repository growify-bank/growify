package org.growify.bank.service.strategy;

import org.growify.bank.dto.request.UptadeRequestDTO;
import org.growify.bank.dto.response.TokenResponseDTO;
import org.growify.bank.dto.response.UserResponseDTO;
import org.growify.bank.exception.TokenInvalidException;
import org.growify.bank.model.token.Token;
import org.growify.bank.model.token.TokenType;
import org.growify.bank.model.user.User;
import org.growify.bank.repository.TokenRepository;
import org.growify.bank.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationTokenManagerStrategy authTokenManager;
    private final AuthenticateValidationStrategy authValidationStrategy;
    private final PasswordValidationStrategy passwordValidationStrategy;
    private final UserIdValidationStrategy userIdValidationStrategy;
    private final EmailAlreadyValidationStrategy emailAlreadyValidationStrategy;
    private final ModelMapper modelMapper;

    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDTO> response = users.stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<UserResponseDTO> getByUserId(String userId) {
        userIdValidationStrategy.validate(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TokenInvalidException("User not found, contact @rafael or @renato"));

        UserResponseDTO response = modelMapper.map(user, UserResponseDTO.class);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<String> deleteUser(String userId) {
        userIdValidationStrategy.validate(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TokenInvalidException("User not found"));

        List<Token> tokens = tokenRepository.findAllByUserId(userId);
        tokenRepository.deleteAll(tokens);

        userRepository.deleteById(userId);
        return ResponseEntity.ok("User and associated tokens deleted successfully");
    }

    public ResponseEntity<TokenResponseDTO> updateUser(
            String userId,
            UptadeRequestDTO updatedUserDto,
            Authentication authentication) {

        userIdValidationStrategy.validate(userId);
        authValidationStrategy.validate(authentication);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TokenInvalidException("User not found"));

        if (updatedUserDto.password() != null && !updatedUserDto.password().isBlank()) {
            passwordValidationStrategy.validate(updatedUserDto.password());
            user.setPassword(passwordEncoder.encode(updatedUserDto.password()));
        }

        if (updatedUserDto.name() != null && !updatedUserDto.name().isBlank()) {
            user.setName(updatedUserDto.name());
        }

        if (updatedUserDto.email() != null && !updatedUserDto.email().isBlank()) {
            emailAlreadyValidationStrategy.validate(updatedUserDto.email());
            user.setEmail(updatedUserDto.email());
        }

        userRepository.save(user);

        List<Token> oldTokens = tokenRepository.findAllByUserId(userId);
        tokenRepository.deleteAll(oldTokens);

        String tokenValue = String.valueOf(authTokenManager.generateTokenResponse(user));

        Token newToken = Token.builder()
                .user(user)
                .tokenValue(tokenValue)
                .tokenType(TokenType.BEARER)
                .tokenRevoked(false)
                .tokenExpired(false)
                .build();

        tokenRepository.save(newToken);

        return ResponseEntity.ok(new TokenResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                tokenValue
        ));
    }
}
