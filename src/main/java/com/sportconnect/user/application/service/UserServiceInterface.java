package com.sportconnect.user.application.service;

import com.sportconnect.user.api.dto.*;
import java.util.List;
import java.util.UUID;

public interface UserServiceInterface {

    UserResponseDTO createUser(CreateUserDTO dto);
    UserResponseDTO updateUser(UUID id, UpdateUserDTO dto);
    UserResponseDTO getUserById(UUID id);
    List<UserResponseDTO> listUsers();
    void deleteUser(UUID id);
}
