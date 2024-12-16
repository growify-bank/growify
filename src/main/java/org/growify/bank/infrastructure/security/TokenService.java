package org.growify.bank.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.growify.bank.exception.TokenGenerationException;
import org.growify.bank.model.token.Token;
import org.growify.bank.model.user.User;
import org.growify.bank.repository.TokenRepository;
import org.growify.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${security.token.private-key}")
    private RSAPrivateKey privateKey;

    @Value("${security.token.public-key}")
    private RSAPublicKey publicKey;

    @Value("${security.token.expiration-token}")
    private Integer expirationToken;

    @Value("${security.token.expiration-refresh-token}")
    private Integer expirationRefreshToken;

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public String generateToken(User user, Integer expiration) {
        try {
            Algorithm algorithm = getAlgorithm();
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withClaim("Name", user.getName())
                    .withAudience(user.getRole().name())
                    .withIssuedAt(Date.from(Instant.now()))
                    .withExpiresAt(getExpirationDate(expiration))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new TokenGenerationException("Error while generating token", exception);
        }
    }

    public String generateAccessToken(User user) {
        return generateToken(user, expirationToken);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, expirationRefreshToken);
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = getAlgorithm();
            DecodedJWT decodedJWT = JWT
                    .require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token);

            Optional<Token> optionalToken = tokenRepository.findByTokenValue(token);
            if (optionalToken.isEmpty()) {
                return null;
            }

            return decodedJWT.getSubject();
        } catch (JWTVerificationException ex) {
            return null;
        }
    }

    public Algorithm getAlgorithm() {
        return Algorithm.RSA256(publicKey, privateKey);
    }

    public Instant getExpirationDate(Integer expiration) {
        return LocalDateTime.now().plusMinutes(expiration).toInstant(ZoneOffset.of("-03:00"));
    }

    public User getUserFromToken(String token) {
        String email = validateToken(token);
        if (email != null) {
            Optional<User> userOptional = userRepository.findByEmail(email); // Usando findByEmail com Optional<User>
            return userOptional.orElse(null);
        }
        return null;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = validateToken(token);
        return (email != null && email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getExpiresAt().before(new Date());
        } catch (JWTVerificationException ex) {
            return false;
        }
    }
}
