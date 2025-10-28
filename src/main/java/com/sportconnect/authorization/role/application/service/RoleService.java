package com.sportconnect.authorization.role.application.service;

import com.sportconnect.authorization.role.api.dto.*;
import com.sportconnect.shared.datatable.dto.DataTableRequest;
import com.sportconnect.shared.datatable.dto.DataTableResponse;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    RoleResponseDTO createRole(CreateRoleDTO dto);

    RoleResponseDTO updateRole(UUID id, UpdateRoleDTO dto);

    DataTableResponse<RoleResponseDTO> getRoles(DataTableRequest request);

    RoleResponseDTO getRoleById(UUID id);

    void deleteRole(UUID id);

    void assignPermissions(UUID roleId, List<UUID> permissionIds);
}
