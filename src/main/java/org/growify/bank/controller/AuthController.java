package org.growify.bank.controller;

import org.growify.bank.dto.request.LoginRequestDTO;
import org.growify.bank.dto.request.RefreshTokenDTO;
import org.growify.bank.dto.request.RegisterRequestDTO;
import org.growify.bank.dto.response.TokenResponseDTO;
import org.growify.bank.service.strategy.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO authDto) {
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return null;
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenResponseDTO> refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        return null;
    }
}
