package org.growify.bank.model.token;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import org.growify.bank.model.user.User;

import java.util.UUID;

@Entity
@Table(schema = "user_tokens")
public class Token {

    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String tokenValue;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType tokenType;

    @Getter
    @Setter
    @Column(nullable = false)
    private boolean tokenRevoked;

    @Getter
    @Setter
    @Column(nullable = false)
    private boolean tokenExpired;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
