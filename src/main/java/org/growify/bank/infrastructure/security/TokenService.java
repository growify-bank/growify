package org.growify.bank.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Service
@RequiredArgsConstructor
public class TokenService {

    private RSAPrivateKey privateKey;

    private RSAPublicKey publicKey;

    private Integer expirationToken;

    private Integer expirationRefreshToken;
}
