package org.growify.bank.repository;

import org.growify.bank.model.token.Token;
import org.growify.bank.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    List<Token> findAllValidTokenByUser(String id);
    List<Token> findAllByUserId(String userId);
    List<Token> findByTokenValue(String token);
    List<Token> deleteByUser(User user);
}
