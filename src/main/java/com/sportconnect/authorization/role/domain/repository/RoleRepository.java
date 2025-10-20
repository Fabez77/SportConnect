package com.sportconnect.authorization.role.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sportconnect.authorization.role.domain.model.Role;

public interface RoleRepository {
    Role save(Role role);
    Optional<Role> findById(UUID id);
    List<Role> findAll();
    void deleteById(UUID id);
}
