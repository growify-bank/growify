package org.growify.bank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.growify.bank.dto.request.LoginRequestDTO;
import org.growify.bank.dto.request.RefreshTokenRequestDTO;
import org.growify.bank.dto.request.RegisterUserRequestDTO;
import org.growify.bank.dto.response.TokenResponseDTO;
import org.growify.bank.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;

    @Operation(
            method = "POST",
            description = "Endpoint for authentication. If a user enters the wrong password more than four times, their account will be locked. Upon successful login, returns a token and email.",
            summary = "Authenticate user by verifying credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"token\": [\"string\"]," +
                                            "\"refreshToken\": [\"string\"]" +
                                            "}"
                            ))),

            @ApiResponse(responseCode = "404", description = "Not found.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"Invalid username or password.\"}"
                            ))),

            @ApiResponse(responseCode = "403", description = "Forbidden.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"Account is locked.\"}"
                            ))),
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO authDto) {
        return accountService.loginAccount(authDto, authenticationManager);
    }

    @Operation(
            method = "POST",
            summary = "Register a new user.",
            description = "Endpoint for user registration." +
                    " Accepts " +
                    "'name' (string), " +
                    " 'email' (string), and" +
                    " 'password' (string)." +
                    " 'confirmPassword' (string)." +
                    " Upon successful registration, returns a token and email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"token\": [\"string\"]," +
                                            "\"refreshToken\": [\"string\"]" +
                                            "}"
                            ))),

            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"name\": [\"Name is required.\", \"Name must be between 5 and 30 characters.\"]," +
                                            "\"email\": [\"Email is required.\", \"Invalid email format.\", \"Email must be between 10 and 30 characters.\"]," +
                                            "\"password\": [\"Password must be strong and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.\", \"Password must be between 10 and 30 characters.\", \"Password is required.\"]," +
                                            "\"confirmPassword\": [\"Password must be strong and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.\", \"Password must be between 10 and 30 characters.\", \"Password is required.\"]," +
                                            "\"message\": [\"Passwords do not match.\"]" +
                                            "}"
                            ))),
            @ApiResponse(responseCode = "409", description = "Conflicts.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"E-mail not available.\"}"
                            )))
    })
    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> register(@RequestBody @Valid RegisterUserRequestDTO registerRequestDTO) {
        return accountService.registerAccount(registerRequestDTO);
    }

    @Operation(
            summary = "Refresh the access token using a refresh token.",
            description = "Endpoint to refresh the access token using a valid refresh token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            }
    )
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return accountService.refreshToken(refreshTokenRequestDTO);
    }

    @Operation(
            method = "POST",
            summary = "Logout the current user.",
            description = "Endpoint to logout the currently authenticated user. " +
                    "Invalidates the current session and clears the security context.",
            responses ={
                    @ApiResponse(responseCode = "204", description = "No content."),
                    @ApiResponse(responseCode = "401", description = "Unauthorized.")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/csrf-token")
    public CsrfToken csrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }
}
