package org.growify.bank.dto.request;

public record UpdateRequestDTO(String name, String email, String password, String confirmPassword) {
}
