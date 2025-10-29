package com.sportconnect.authorization.role.infrastructure.persistence.repository;

import com.sportconnect.authorization.role.infrastructure.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface JpaRoleRepository extends JpaRepository<RoleEntity, UUID>, JpaSpecificationExecutor<RoleEntity> {
}
