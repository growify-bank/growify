package org.growify.bank.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDTO(

        @NotBlank(message = "Name is required.")
        @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters.")
        String name,

        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        @Size(min = 10, max = 30, message = "Email must be between 10 and 30 characters.")
        String email,

        @NotBlank(message = "Password is required.")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+).{10,30}$",
                message = "Password must be strong and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
        @Size(min = 10, max = 30, message = "Password must be between 10 and 30 characters.")
        String password,

        @NotBlank(message = "Confirm password is required.")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+).{10,30}$",
                message = "Password must be strong and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
        @Size(min = 10, max = 30, message = "Password must be between 10 and 30 characters.")
        String confirmPassword
) {
}
