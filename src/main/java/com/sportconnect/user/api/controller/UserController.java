package com.sportconnect.user.api.controller;

import com.sportconnect.user.api.dto.*;
import com.sportconnect.user.application.service.UserServiceInterface;
import com.sportconnect.shared.dto.ApiResponse;
import com.sportconnect.shared.service.ApiResponseServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceInterface userService;
    private final ApiResponseServiceInterface responseService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@RequestBody @Valid CreateUserDTO dto) {
        UserResponseDTO user = userService.createUser(dto);
        return responseService.success(HttpStatus.CREATED, "Usuario creado", user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserDTO dto) {
        UserResponseDTO updated = userService.updateUser(id, dto);
        return responseService.success(HttpStatus.OK, "Usuario actualizado", updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable UUID id) {
        UserResponseDTO user = userService.getUserById(id);
        return responseService.success(HttpStatus.OK, "Usuario encontrado", user);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> listUsers() {
        List<UserResponseDTO> users = userService.listUsers();
        return responseService.success(HttpStatus.OK, "Lista de usuarios", users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return responseService.success(HttpStatus.NO_CONTENT, "Usuario eliminado");
    }
}
