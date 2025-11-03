package com.sportconnect.authorization.permission.domain.repository;

import com.sportconnect.authorization.permission.domain.model.Permission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository {
    Permission save(Permission permission);

    Optional<Permission> findById(UUID id);

    List<Permission> findAll();

    Page<Permission> findAll(Map<String, String> filters, String search, Pageable pageable);

    void deleteById(UUID id);

    List<Permission> findAllById(List<UUID> ids);
}
