package com.sportconnect.user.api.controller;

import com.sportconnect.shared.apiresponse.dto.ApiResponse;
import com.sportconnect.shared.apiresponse.service.ApiResponseService;
import com.sportconnect.shared.datatable.dto.*;
import com.sportconnect.user.api.dto.*;
import com.sportconnect.user.application.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ApiResponseService responseService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> create(@Valid @RequestBody CreateUserDTO dto) {
        UserResponseDTO user = userService.createUser(dto);
        return responseService.success(HttpStatus.CREATED, "Usuario creado", user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserDTO dto) {
        UserResponseDTO updated = userService.updateUser(id, dto);
        return responseService.success(HttpStatus.OK, "Usuario actualizado", updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getById(@PathVariable UUID id) {
        UserResponseDTO user = userService.getUserById(id);
        return responseService.success(HttpStatus.OK, "Usuario encontrado", user);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DataTableResponse<UserResponseDTO>>> getUsers(DataTableRequest request) {
        DataTableResponse<UserResponseDTO> users = userService.getUsers(request);
        return responseService.success(HttpStatus.OK, "Lista de usuarios paginada", users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        userService.deleteUser(id);
        return responseService.success(HttpStatus.NO_CONTENT, "Usuario eliminado");
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<ApiResponse<UserRoleResponse>> getUserRoles(@PathVariable UUID id) {
        UserRoleResponse response = userService.getUserRolesIds(id);
        return responseService.success(HttpStatus.OK, "Lista de roles", response);
    }

    @PutMapping("/{userId}/roles")
    public ResponseEntity<ApiResponse<Void>> assignRoles(
            @PathVariable UUID userId,
            @RequestBody AssignRolesRequest request) {

        userService.assignRoles(userId, request.roleIds());

        return responseService.success(
                HttpStatus.NO_CONTENT,
                "Roles asignados correctamente");
    }
}
