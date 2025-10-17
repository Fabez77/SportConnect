package com.sportconnect.auth.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import java.util.UUID;

public interface UserAuthJpaRepository extends JpaRepository<UserAuthEntity, UUID> {
    Optional<UserAuthEntity> findByUsername(String username);
    Optional<UserAuthEntity> findByEmail(String email);
}