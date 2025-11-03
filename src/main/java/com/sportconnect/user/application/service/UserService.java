package com.sportconnect.user.application.service;

import com.sportconnect.user.api.dto.*;
import com.sportconnect.shared.datatable.dto.DataTableRequest;
import com.sportconnect.shared.datatable.dto.DataTableResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseDTO createUser(CreateUserDTO dto);

    UserResponseDTO updateUser(UUID id, UpdateUserDTO dto);

    UserResponseDTO getUserById(UUID id);

    DataTableResponse<UserResponseDTO> getUsers(DataTableRequest request);

    void deleteUser(UUID id);

    void assignRoles(UUID userId, List<UUID> roleIds);

    UserRoleResponse getUserRolesIds(UUID userId);
}

