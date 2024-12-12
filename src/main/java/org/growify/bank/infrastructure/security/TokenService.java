package org.growify.bank.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

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
}
