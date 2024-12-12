package org.growify.bank.model.token;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.growify.bank.model.user.User;

@Data
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String tokenValue;
    private TokenType tokenType;
    private boolean tokenRevoked;
    private boolean tokenExpired;
    private User user;
}
