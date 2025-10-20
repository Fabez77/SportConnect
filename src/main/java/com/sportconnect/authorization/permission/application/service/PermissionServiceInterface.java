package com.sportconnect.authorization.permission.application.service;

import com.sportconnect.authorization.permission.api.dto.*;
import java.util.List;
import java.util.UUID;

public interface PermissionServiceInterface {
    PermissionResponseDTO createPermission(CreatePermissionDTO dto);
    PermissionResponseDTO updatePermission(UUID id, UpdatePermissionDTO dto);
    List<PermissionResponseDTO> getAllPermissions();
    PermissionResponseDTO getPermissionById(UUID id);
    void deletePermission(UUID id);
}
