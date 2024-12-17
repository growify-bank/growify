package org.growify.bank.service.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.growify.bank.exception.EmailAlreadyExistsException;
import org.growify.bank.model.user.User;
import org.growify.bank.repository.UserRepository;
import org.growify.bank.service.strategy.interfaces.EmailAlreadyValidationStrategy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailAlreadyValidationImpl implements EmailAlreadyValidationStrategy {

    private final UserRepository userRepository;

    @Override
    public void validate(String existingEmail, String newEmail, String userIdToExclude) {
        if (!existingEmail.equals(newEmail) && checkIfEmailAlreadyExists(newEmail, userIdToExclude)) {
            throw new EmailAlreadyExistsException();
        }
    }

    private boolean checkIfEmailAlreadyExists(String email, String userIdToExclude) {
        User user = (User) userRepository.findByEmail(email);
        return user != null && !user.getId().equals(userIdToExclude);
    }
}
