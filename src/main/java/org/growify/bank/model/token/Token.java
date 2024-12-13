package org.growify.bank.model.token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.growify.bank.model.user.User;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Table(schema = "user_tokens")
@Entity public class Token {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id private String id;

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
