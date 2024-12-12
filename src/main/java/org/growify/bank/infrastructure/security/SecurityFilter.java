package org.growify.bank.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.growify.bank.repository.TokenRepository;
import org.growify.bank.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    final TokenService tokenService;
    final TokenRepository tokenRepository;
    final UserRepository userRepository;
    final HttpServletRequest httpServletRequest;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
    }

    public String recoverTokenFromRequest(HttpServletRequest request){
        return "";
    }

    public void handleAuthentication(String token, HttpServletResponse response){

    }

    public boolean isTokenValid(String token){
        return false;
    }

    public void setAuthenticationInSecurityContext(UserDetails userDetails){

    }

    public void handleInvalidToken(HttpServletResponse response, Exception e) throws IOException{
        
    }
}
