package org.growify.bank.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
public class UserSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/v1/users").hasRole(ADMIN)
                .requestMatchers(HttpMethod.GET, "/v1/users/find/{id}").hasRole(ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/v1/users/delete/{id}").hasRole(ADMIN)
                .requestMatchers(HttpMethod.PUT, "/v1/users/update/{id}").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.POST, "/v1/users/change-password").hasAnyRole(ADMIN, USER)
        );
    }
}
