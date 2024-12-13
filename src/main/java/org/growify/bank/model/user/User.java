package org.growify.bank.model.user;

import jakarta.persistence.*;
import lombok.Data;

import org.growify.bank.model.token.Token;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(schema = "users", name = "users")
public class User {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokens;

    public User() {
    }
}
