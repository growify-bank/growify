package org.growify.bank.model.token;

import jakarta.persistence.*;
<<<<<<< HEAD
import lombok.Getter;
import lombok.Setter;

import org.growify.bank.model.user.User;

import java.util.UUID;
=======
import lombok.*;
import org.growify.bank.model.user.User;
>>>>>>> b24df8af8bc7f8894831e40516cbbea2999bf1c1

@Entity
@Table(schema = "user_tokens")
<<<<<<< HEAD
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
=======
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
>>>>>>> b24df8af8bc7f8894831e40516cbbea2999bf1c1
    private User user;
}
