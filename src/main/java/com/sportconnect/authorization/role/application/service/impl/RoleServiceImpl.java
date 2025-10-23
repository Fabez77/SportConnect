package com.sportconnect.authorization.role.application.service.impl;

import org.springframework.stereotype.Service;

import com.sportconnect.authorization.role.api.dto.CreateRoleDTO;
import com.sportconnect.authorization.role.api.dto.RoleResponseDTO;
import com.sportconnect.authorization.role.api.dto.UpdateRoleDTO;
import com.sportconnect.authorization.role.api.mapper.RoleDtoMapper;
import com.sportconnect.authorization.role.application.service.RoleService;
import com.sportconnect.authorization.role.domain.model.Role;
import com.sportconnect.authorization.role.domain.repository.RoleRepository;
import com.sportconnect.authorization.permission.domain.model.Permission;
import com.sportconnect.authorization.permission.domain.repository.PermissionRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final PermissionRepository permissionRepository;
    private final RoleDtoMapper mapper;

    public RoleServiceImpl(RoleRepository repository,
            PermissionRepository permissionRepository,
            RoleDtoMapper mapper) {
        this.repository = repository;
        this.permissionRepository = permissionRepository;
        this.mapper = mapper;
    }

    @Override
    public RoleResponseDTO createRole(CreateRoleDTO dto) {
        Role role = mapper.toDomain(dto);

        // Resolver permisos reales desde la BD
        Set<Permission> permissions = dto.getPermissions().stream()
                .map(id -> permissionRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Permiso no encontrado: " + id)))
                .collect(Collectors.toSet());

        role.setPermissions(permissions);

        Role saved = repository.save(role);
        return mapper.toResponse(saved);
    }

    @Override
    public RoleResponseDTO updateRole(UUID id, UpdateRoleDTO dto) {
        Role role = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        mapper.updateDomain(dto, role);

        // Resolver permisos reales desde la BD (si vienen en el DTO)
        if (dto.getPermissions() != null) {
            Set<Permission> permissions = dto.getPermissions().stream()
                    .map(pid -> permissionRepository.findById(pid)
                            .orElseThrow(() -> new RuntimeException("Permiso no encontrado: " + pid)))
                    .collect(Collectors.toSet());
            role.setPermissions(permissions);
        }

        Role updated = repository.save(role);
        return mapper.toResponse(updated);
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public RoleResponseDTO getRoleById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }

    @Override
    public void deleteRole(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void assignPermissions(UUID roleId, List<UUID> permissionIds) {
        Role role = repository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

        List<Permission> permissions = permissionRepository.findAllById(permissionIds);

        if (permissions.size() != permissionIds.size()) {
            throw new IllegalArgumentException("Algunos permisos no existen");
        }

        role.setPermissions(new HashSet<>(permissions));
        repository.save(role);
    }

}
