package com.sportconnect.authorization.permission.domain.repository;

import com.sportconnect.authorization.permission.domain.model.Permission;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository {
    Permission save(Permission permission);
    Optional<Permission> findById(UUID id);
    List<Permission> findAll();
    void deleteById(UUID id);
}
