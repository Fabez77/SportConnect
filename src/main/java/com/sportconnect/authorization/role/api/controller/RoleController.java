package com.sportconnect.authorization.role.api.controller;

import com.sportconnect.authorization.role.api.dto.*;
import com.sportconnect.authorization.role.application.service.RoleServiceInterface;
import com.sportconnect.shared.dto.ApiResponse;
import com.sportconnect.shared.service.ApiResponseServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleServiceInterface roleService;
    private final ApiResponseServiceInterface responseService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponseDTO>> create(@Valid @RequestBody CreateRoleDTO dto) {
        RoleResponseDTO role = roleService.createRole(dto);
        return responseService.success(HttpStatus.CREATED, "Rol creado", role);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> update(@PathVariable UUID id, @Valid @RequestBody UpdateRoleDTO dto) {
        RoleResponseDTO updated = roleService.updateRole(id, dto);
        return responseService.success(HttpStatus.OK, "Rol actualizado", updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> getById(@PathVariable UUID id) {
        RoleResponseDTO role = roleService.getRoleById(id);
        return responseService.success(HttpStatus.OK, "Rol encontrado", role);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponseDTO>>> getAll() {
        List<RoleResponseDTO> roles = roleService.getAllRoles();
        return responseService.success(HttpStatus.OK, "Lista de roles", roles);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        roleService.deleteRole(id);
        return responseService.success(HttpStatus.NO_CONTENT, "Rol eliminado");
    }
}
