package com.sportconnect.authorization.role.application.service;

import org.springframework.stereotype.Service;

import com.sportconnect.authorization.role.api.dto.CreateRoleDTO;
import com.sportconnect.authorization.role.api.dto.RoleResponseDTO;
import com.sportconnect.authorization.role.api.dto.UpdateRoleDTO;
import com.sportconnect.authorization.role.api.mapper.RoleDtoMapper;
import com.sportconnect.authorization.role.domain.model.Role;
import com.sportconnect.authorization.role.domain.repository.RoleRepository;

import java.util.List;
import java.util.UUID;

@Service
public class RoleService implements RoleServiceInterface {

    private final RoleRepository repository;
    private final RoleDtoMapper mapper;

    public RoleService(RoleRepository repository, RoleDtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public RoleResponseDTO createRole(CreateRoleDTO dto) {
        Role role = mapper.toDomain(dto);
        return mapper.toResponse(repository.save(role));
    }

    @Override
    public RoleResponseDTO updateRole(UUID id, UpdateRoleDTO dto) {
        Role role = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        mapper.updateDomain(dto, role);
        return mapper.toResponse(repository.save(role));
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public RoleResponseDTO getRoleById(UUID id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }

    @Override
    public void deleteRole(UUID id) {
        repository.deleteById(id);
    }
}
