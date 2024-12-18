package org.growify.bank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDTO(

        @NotBlank(message = "Old password is required.")
        String oldPassword,

        @NotBlank(message = "Confirm password is required.")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+).{10,30}$",
                message = "Password must be strong and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
        @Size(min = 10, max = 30, message = "Password must be between 10 and 30 characters.")
        String newPassword) {
}
