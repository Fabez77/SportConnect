package com.sportconnect.authorization.permission.application.service;

import com.sportconnect.authorization.permission.api.dto.*;
import com.sportconnect.authorization.permission.api.mapper.PermissionDtoMapper;
import com.sportconnect.authorization.permission.domain.model.Permission;
import com.sportconnect.authorization.permission.domain.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PermissionService implements PermissionServiceInterface {

    private final PermissionRepository repository;
    private final PermissionDtoMapper mapper;

    public PermissionService(PermissionRepository repository, PermissionDtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PermissionResponseDTO createPermission(CreatePermissionDTO dto) {
        Permission permission = mapper.toDomain(dto);
        return mapper.toResponse(repository.save(permission));
    }

    @Override
    public PermissionResponseDTO updatePermission(UUID id, UpdatePermissionDTO dto) {
        Permission permission = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
        mapper.updateDomain(dto, permission);
        return mapper.toResponse(repository.save(permission));
    }

    @Override
    public List<PermissionResponseDTO> getAllPermissions() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public PermissionResponseDTO getPermissionById(UUID id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
    }

    @Override
    public void deletePermission(UUID id) {
        repository.deleteById(id);
    }
}
