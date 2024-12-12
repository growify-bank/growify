package org.growify.bank.infrastructure.security;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.growify.bank.model.user.User;
import org.growify.bank.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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

    public String generateToken(User user, Integer expiration){
        return "";
    }

    public String generateAccessToken(User user){
        return "";
    }

    public String generateRefreshToken(User user){
        return "";
    }

    public String validateToken(String token){
        return  "";
    }

    public Algorithm getAlgorithm(){
        return Algorithm.RSA256(publicKey, privateKey);
    }

    public Instant getExpirationDate(Integer expiration){
        return LocalDateTime.now().plusMinutes(expiration).toInstant(ZoneOffset.of("-03:00"));
    }
}
