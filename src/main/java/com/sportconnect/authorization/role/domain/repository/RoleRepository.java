package com.sportconnect.authorization.role.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sportconnect.authorization.role.domain.model.Role;

import java.util.Map;

public interface RoleRepository {
    Role save(Role role);
    Optional<Role> findById(UUID id);
    List<Role> findAll();
    
    Page<Role> findAll(Map<String, String> filters, String search, Pageable pageable);

    void deleteById(UUID id);
    List<Role> findAllById(List<UUID> ids);
}
