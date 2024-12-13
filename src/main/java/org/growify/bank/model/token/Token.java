package org.growify.bank.model.token;

import jakarta.persistence.*;
import lombok.Data;  

import org.growify.bank.model.user.User;

import java.util.UUID;

@Entity
@Table(schema = "user_tokens")
@Data
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String tokenValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType tokenType;

    @Column(nullable = false)
    private boolean tokenRevoked;

    @Column(nullable = false)
    private boolean tokenExpired;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
