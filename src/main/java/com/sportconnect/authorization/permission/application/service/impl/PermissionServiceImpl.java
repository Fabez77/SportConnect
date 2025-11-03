package com.sportconnect.authorization.permission.application.service.impl;

import com.sportconnect.authorization.permission.api.dto.*;
import com.sportconnect.authorization.permission.api.mapper.PermissionDtoMapper;
import com.sportconnect.authorization.permission.application.service.PermissionService;
import com.sportconnect.authorization.permission.domain.model.Permission;
import com.sportconnect.authorization.permission.domain.repository.PermissionRepository;
import com.sportconnect.shared.datatable.dto.DataTableRequest;
import com.sportconnect.shared.datatable.dto.DataTableResponse;
import com.sportconnect.shared.datatable.service.DataTableService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor // genera constructor con todos los campos final
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository repository;
    private final PermissionDtoMapper mapper;
    private final DataTableService dataTableService;

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
    public DataTableResponse<PermissionResponseDTO> getPermissions(DataTableRequest request) {
        Pageable pageable = dataTableService.buildPageable(request);

        // El servicio NO construye Specification, solo pasa filtros y search
        Page<Permission> page = repository.findAll(
                request.getFilters(),
                request.getSearch(),
                pageable);

        Page<PermissionResponseDTO> dtoPage = page.map(mapper::toResponse);
        return dataTableService.buildResponse(dtoPage);
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
