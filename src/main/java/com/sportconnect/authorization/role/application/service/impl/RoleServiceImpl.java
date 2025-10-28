package com.sportconnect.authorization.role.application.service.impl;

import com.sportconnect.authorization.role.api.dto.*;
import com.sportconnect.authorization.role.api.mapper.RoleDtoMapper;
import com.sportconnect.authorization.role.application.service.RoleService;
import com.sportconnect.authorization.role.domain.model.Role;
import com.sportconnect.authorization.role.domain.repository.RoleRepository;
import com.sportconnect.authorization.role.infrastructure.persistence.entity.RoleEntity;
import com.sportconnect.authorization.permission.domain.model.Permission;
import com.sportconnect.authorization.permission.domain.repository.PermissionRepository;
import com.sportconnect.shared.datatable.dto.DataTableRequest;
import com.sportconnect.shared.datatable.dto.DataTableResponse;
import com.sportconnect.shared.datatable.filter.SpecificationBuilder;
import com.sportconnect.shared.datatable.service.DataTableService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final PermissionRepository permissionRepository;
    private final RoleDtoMapper mapper;
    private final DataTableService dataTableService;

    @Override
    public RoleResponseDTO createRole(CreateRoleDTO dto) {
        Role role = mapper.toDomain(dto);

        // Resolver permisos desde la BD
        Set<Permission> permissions = dto.getPermissions().stream()
                .map(id -> permissionRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Permiso no encontrado: " + id)))
                .collect(Collectors.toSet());

        role.setPermissions(permissions);

        return mapper.toResponse(repository.save(role));
    }

    @Override
    public RoleResponseDTO updateRole(UUID id, UpdateRoleDTO dto) {
        Role role = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        mapper.updateDomain(dto, role);

        if (dto.getPermissions() != null) {
            Set<Permission> permissions = dto.getPermissions().stream()
                    .map(pid -> permissionRepository.findById(pid)
                            .orElseThrow(() -> new RuntimeException("Permiso no encontrado: " + pid)))
                    .collect(Collectors.toSet());
            role.setPermissions(permissions);
        }

        return mapper.toResponse(repository.save(role));
    }

    @Override
    public DataTableResponse<RoleResponseDTO> getRoles(DataTableRequest request) {
        Pageable pageable = dataTableService.buildPageable(request);

        SpecificationBuilder<RoleEntity> builder = new SpecificationBuilder<>();
        Specification<RoleEntity> spec = builder.build(
                request.getFilters(),
                request.getSearch(),
                List.of("name", "description") // campos filtrables
        );

        Page<Role> page = repository.findAll(spec, pageable);
        Page<RoleResponseDTO> dtoPage = page.map(mapper::toResponse);

        return dataTableService.buildResponse(dtoPage);
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

