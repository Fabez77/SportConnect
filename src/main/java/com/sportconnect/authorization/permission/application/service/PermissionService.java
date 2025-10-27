package com.sportconnect.authorization.permission.application.service;

import com.sportconnect.authorization.permission.api.dto.*;
import com.sportconnect.shared.datatable.dto.DataTableRequest;
import com.sportconnect.shared.datatable.dto.DataTableResponse;

import java.util.UUID;

public interface PermissionService {
    PermissionResponseDTO createPermission(CreatePermissionDTO dto);

    PermissionResponseDTO updatePermission(UUID id, UpdatePermissionDTO dto);

    DataTableResponse<PermissionResponseDTO> getPermissions(DataTableRequest request);

    PermissionResponseDTO getPermissionById(UUID id);

    void deletePermission(UUID id);
}
