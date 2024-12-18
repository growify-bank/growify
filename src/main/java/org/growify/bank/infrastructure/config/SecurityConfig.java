package org.growify.bank.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.growify.bank.infrastructure.security.CustomLogoutHandler;
import org.growify.bank.infrastructure.security.SecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    final SecurityFilter securityFilter;
    final AuthenticateSecurityConfig authSecurityConfig;
    final UserSecurityConfig userSecurityConfig;
    final CustomLogoutHandler logoutHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }

    /**
     * Configures the security filter chain for the application.
     * <p>
     * This method applies specific security configurations for authentication and user details,
     * sets up CSRF protection with a custom cookie CSRF token repository, configures session management
     * to be stateless, and defines authorization rules for various endpoints. It also configures a custom
     * filter for processing security-related tasks before the standard username and password authentication
     * filter, and sets up custom logout behavior.
     * </p>
     * <p>
     * To add new routes in an organized way, follow the pattern used in the AuthSecurityConfig and UserSecurityConfig classes.
     * Create separate classes to configure the authorization rules for each set of endpoints. In these classes, define specific
     * methods to apply the corresponding security settings to the desired endpoints, similar to the methods
     * used in AuthSecurityConfig and UserSecurityConfig. When you have finished configuring a new class, integrate it into the chain of
     * application's security filters, without the need to use the applyAuthSecurityConfig or applyUserSecurityConfig methods,
     * by creating an instance of the class directly in the configure(HttpSecurity http) method of the main security configuration class.
     * </p>
     *
     * @param httpSecurity the {@link HttpSecurity} to configure
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during the configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        applyAuthSecurityConfig(httpSecurity);
        applyUserSecurityConfig(httpSecurity);

        return httpSecurity
                .csrf(csrf -> csrf.csrfTokenRepository(customizeCookieCsrfTokenRepository()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/auth/**",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html"
                        ).permitAll().anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/v1/auth/logout")
                        .logoutSuccessUrl("/v1/auth/login")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    private void applyAuthSecurityConfig(HttpSecurity http) throws Exception {
        authSecurityConfig.configure(http);
    }

    private void applyUserSecurityConfig(HttpSecurity http) throws Exception {
        userSecurityConfig.configure(http);
    }

    private CsrfTokenRepository customizeCookieCsrfTokenRepository() {
        CookieCsrfTokenRepository tokenRepository = new CookieCsrfTokenRepository();
        tokenRepository.setCookieCustomizer(builder ->
                builder
                        .httpOnly(true)
                        .sameSite("None")
                        .secure(true)
        );
        return tokenRepository;
    }
}
