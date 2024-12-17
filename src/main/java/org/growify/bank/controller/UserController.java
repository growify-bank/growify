package org.growify.bank.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.growify.bank.model.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    // Endpoint para listar todos os usuários
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return null;
    }

    // Endpoint para obter um usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return null;
    }

    // Endpoint para atualizar um usuário
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return null;
    }

    // Endpoint para deletar um usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(HttpServletRequest request, @PathVariable Long id) {

        return null;
    }
}
