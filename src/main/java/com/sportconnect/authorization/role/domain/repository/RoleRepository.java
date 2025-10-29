package com.sportconnect.authorization.role.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import com.sportconnect.authorization.role.domain.model.Role;
import com.sportconnect.authorization.role.infrastructure.persistence.entity.RoleEntity;

public interface RoleRepository {
    Role save(Role role);
    Optional<Role> findById(UUID id);
    List<Role> findAll();
    Page<Role> findAll(Specification<RoleEntity> spec, Pageable pageable);
    void deleteById(UUID id);
    List<Role> findAllById(List<UUID> ids);
}
