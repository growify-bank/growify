package org.growify.bank.utils;

import lombok.RequiredArgsConstructor;
import org.growify.bank.model.user.User;
import org.growify.bank.model.user.UserRole;
import org.growify.bank.repository.TokenRepository;
import org.growify.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class Instantiation implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${initiation.admin.password}")
    private String adminPassword;

    @Value("${initiation.user.password}")
    private String userPassword;

    @Override
    public void run(String... args) {
        tokenRepository.deleteAll();

        userRepository.deleteAll();

        String encryptedAdminPassword = passwordEncoder.encode(adminPassword);
        User admin = new User(
                "Admin",
                "admin@localhost.com",
                encryptedAdminPassword,
                true,
                UserRole.ADMIN);
        userRepository.save(admin);

        String encryptedUserPassword = passwordEncoder.encode(userPassword);
        User user = new User(
                "User",
                "user@localhost.com",
                encryptedUserPassword,
                true,
                UserRole.USER);
        userRepository.save(user);
    }
}
