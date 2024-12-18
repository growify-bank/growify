package org.growify.bank.service;

import lombok.RequiredArgsConstructor;
import org.growify.bank.dto.request.ChangePasswordRequestDTO;
import org.growify.bank.dto.request.UpdateUserRequestDTO;
import org.growify.bank.dto.response.TokenResponseDTO;
import org.growify.bank.dto.response.UserResponseDTO;
import org.growify.bank.exception.user.UserNotFoundException;
import org.growify.bank.exception.user.InvalidOldPasswordException;
import org.growify.bank.model.user.User;
import org.growify.bank.repository.TokenRepository;
import org.growify.bank.repository.UserRepository;
import org.growify.bank.service.strategy.interfaces.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
    private final ModelMapper modelMapper;

    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found");
        }

        List<UserResponseDTO> response = new ArrayList<>();
        users.forEach(x -> response.add(modelMapper.map(x, UserResponseDTO.class)));

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<UserResponseDTO> getByUserId(String userId) {

        User existingUser = findUserByIdOrThrow(userId);

        UserResponseDTO userResponse = modelMapper.map(existingUser, UserResponseDTO.class);
        return ResponseEntity.ok(userResponse);
    }

    @Transactional
    public ResponseEntity<Void> deleteUser(String userId) {

        User existingUser = findUserByIdOrThrow(userId);

        tokenRepository.deleteByUser(existingUser);

        userRepository.delete(existingUser);

        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<TokenResponseDTO> updateUser(String userId, UpdateUserRequestDTO updatedUserDto, Authentication authentication) {

        User existingUser = findUserByIdOrThrow(userId);

        authValidationStrategy.validate(authentication);

        passwordValidationStrategy.validate(updatedUserDto.password(), updatedUserDto.confirmPassword());

        userIdValidationStrategy.validateUserId(authentication, userId);

        validateEmailUpdate(existingUser, updatedUserDto);
        updateUserProperties(existingUser, updatedUserDto);
        userRepository.save(existingUser);

        authTokenManager.revokeAllUserTokens(existingUser);

        return ResponseEntity.ok(authTokenManager.generateTokenResponse(existingUser));
    }

    @Transactional
    public ResponseEntity<Void> changePassword(ChangePasswordRequestDTO changePasswordRequestDTO, Authentication authentication) {

        authValidationStrategy.validate(authentication);

        User user = (User) authentication.getPrincipal();
        User existingUser = findUserByIdOrThrow(user.getId());

        if (!passwordEncoder.matches(changePasswordRequestDTO.oldPassword(), existingUser.getPassword())) {
            throw new InvalidOldPasswordException();
        }

        existingUser.setPassword(passwordEncoder.encode(changePasswordRequestDTO.newPassword()));
        userRepository.save(existingUser);

        return ResponseEntity.noContent().build();
    }

    private User findUserByIdOrThrow(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with id:" + userId));
    }

    protected void updateUserProperties(User existingUser, UpdateUserRequestDTO updatedUserDto) {
        updateField(existingUser::setName, existingUser.getName(), updatedUserDto.name());
        updateField(existingUser::setEmail, existingUser.getEmail(), updatedUserDto.email());
        updatePassword(existingUser, updatedUserDto.password());
    }

    protected void updatePassword(User existingUser, String newPassword) {
        if (StringUtils.hasText(newPassword) && !passwordEncoder.matches(newPassword, existingUser.getPassword())) {
            String encryptedPassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(encryptedPassword);
        }
    }

    protected <T> void updateField(Consumer<T> setter, T currentValue, T newValue) {
        if (newValue != null && !newValue.equals(currentValue)) {
            setter.accept(newValue);
        }
    }

    private void validateEmailUpdate(User existingUser, UpdateUserRequestDTO updatedUserDto) {
        String newEmail = updatedUserDto.email();

        emailAlreadyValidationStrategy.validate(
                existingUser.getEmail(),
                newEmail,
                existingUser.getId()
        );
    }
}
