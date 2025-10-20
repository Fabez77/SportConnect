package com.sportconnect.user.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sportconnect.user.infrastructure.persistence.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
    boolean existsByDni(String dni);
}
