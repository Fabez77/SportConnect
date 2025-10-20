package com.sportconnect.authorization.role.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sportconnect.authorization.role.infrastructure.persistence.entity.RoleEntity;

import java.util.UUID;

public interface JpaRoleRepository extends JpaRepository<RoleEntity, UUID> {
}
