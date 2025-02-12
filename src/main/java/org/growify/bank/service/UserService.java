package org.growify.bank.service;

import lombok.RequiredArgsConstructor;

import org.growify.bank.dto.request.ChangePasswordRequestDTO;
import org.growify.bank.dto.request.UpdateUserRequestDTO;
import org.growify.bank.dto.response.TokenResponseDTO;
import org.growify.bank.exception.user.UserNotFoundException;
import org.growify.bank.exception.user.InvalidOldPasswordException;
import org.growify.bank.model.user.User;
import org.growify.bank.repository.TokenRepository;
import org.growify.bank.repository.UserRepository;
import org.growify.bank.service.strategy.interfaces.AuthenticateValidationStrategy;
import org.growify.bank.service.strategy.interfaces.AuthenticationTokenManagerStrategy;
import org.growify.bank.service.strategy.interfaces.EmailAlreadyValidationStrategy;
import org.growify.bank.service.strategy.interfaces.PasswordValidationStrategy;
import org.growify.bank.service.strategy.interfaces.UserIdValidationStrategy;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Consumer;

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

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty())
            throw new UserNotFoundException("No users found");

        return users;
    }

    public User getByUserId(String id) {
        return findUserByIdOrThrow(id);
    }

    @Transactional
    public void deleteUser(String id) {
        User user = findUserByIdOrThrow(id);
        tokenRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    @Transactional
    public TokenResponseDTO updateUser(String id, UpdateUserRequestDTO request, Authentication authentication) {
        User user = findUserByIdOrThrow(id);
        authValidationStrategy.validate(authentication);
        passwordValidationStrategy.validate(request.password(), request.confirmPassword());
        userIdValidationStrategy.validateUserId(authentication, id);
        validateEmailUpdate(user, request);
        updateUserProperties(user, request);
        userRepository.save(user);
        authTokenManager.revokeAllUserTokens(user);
        return authTokenManager.generateTokenResponse(user);
    }

    @Transactional
    public void changePassword(ChangePasswordRequestDTO request, Authentication authentication) {
        authValidationStrategy.validate(authentication);

        User user = findUserByIdOrThrow(((User) authentication.getPrincipal()).getId());
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new InvalidOldPasswordException();
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    private User findUserByIdOrThrow(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with id: " + id));
    }

    private void updateUserProperties(User target, UpdateUserRequestDTO request) {
        updateField(target::setName, target.getName(), request.name());
        updateField(target::setEmail, target.getEmail(), request.email());
        updatePassword(target, request.password());
    }

    private void updatePassword(User target, String newPassword) {
        if (StringUtils.hasText(newPassword) && !passwordEncoder.matches(newPassword, target.getPassword())) {
            String encryptedPassword = passwordEncoder.encode(newPassword);
            target.setPassword(encryptedPassword);
        }
    }

    private <T> void updateField(Consumer<T> setter, T currentValue, T newValue) {
        if (newValue != null && !newValue.equals(currentValue)) {
            setter.accept(newValue);
        }
    }

    private void validateEmailUpdate(User target, UpdateUserRequestDTO request) {
        emailAlreadyValidationStrategy.validate(
                target.getEmail(),
                request.email(),
                target.getId()
        );
    }
}
