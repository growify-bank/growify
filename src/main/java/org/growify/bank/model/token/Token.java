package org.growify.bank.model.token;

import jakarta.persistence.*;
import lombok.*;
import org.growify.bank.model.user.User;

@Data
@Builder
@Entity
@Getter
@Setter
@Table(name = "tb_tokens")
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 600)
    private String tokenValue;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    private boolean tokenRevoked;

    private boolean tokenExpired;

    @ManyToOne
    private User user;
}
