package com.sportconnect.authorization.permission.domain.repository;

import com.sportconnect.authorization.permission.domain.model.Permission;
import com.sportconnect.authorization.permission.infrastructure.persistence.entity.PermissionEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository {
    Permission save(Permission permission);

    Optional<Permission> findById(UUID id);

    List<Permission> findAll();

    Page<Permission> findAll(Specification<PermissionEntity> spec, Pageable pageable); // 👈 nuevo

    void deleteById(UUID id);

    List<Permission> findAllById(List<UUID> ids);
}
