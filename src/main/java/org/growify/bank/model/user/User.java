package org.growify.bank.model.user;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;

import java.util.UUID;

@Table(schema = "users")
@Entity public class User {
    @Getter
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id private UUID id;
}
