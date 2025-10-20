package com.sportconnect.authorization.permission.infrastructure.persistence.repository;

import com.sportconnect.authorization.permission.infrastructure.persistence.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaPermissionRepository extends JpaRepository<PermissionEntity, UUID> {
}
