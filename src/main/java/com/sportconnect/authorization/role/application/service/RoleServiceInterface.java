package com.sportconnect.authorization.role.application.service;

import java.util.List;
import java.util.UUID;

import com.sportconnect.authorization.role.api.dto.CreateRoleDTO;
import com.sportconnect.authorization.role.api.dto.RoleResponseDTO;
import com.sportconnect.authorization.role.api.dto.UpdateRoleDTO;

public interface RoleServiceInterface {
    RoleResponseDTO createRole(CreateRoleDTO dto);
    RoleResponseDTO updateRole(UUID id, UpdateRoleDTO dto);
    List<RoleResponseDTO> getAllRoles();
    RoleResponseDTO getRoleById(UUID id);
    void deleteRole(UUID id);
}
