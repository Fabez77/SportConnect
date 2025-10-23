package com.sportconnect.authorization.permission.api.controller;

import com.sportconnect.authorization.permission.api.dto.*;
import com.sportconnect.authorization.permission.application.service.PermissionServiceInterface;
import com.sportconnect.shared.dto.ApiResponse;
import com.sportconnect.shared.service.ApiResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionServiceInterface permissionService;
    private final ApiResponseService responseService;

    @PostMapping
    public ResponseEntity<ApiResponse<PermissionResponseDTO>> create(@Valid @RequestBody CreatePermissionDTO dto) {
        PermissionResponseDTO permission = permissionService.createPermission(dto);
        return responseService.success(HttpStatus.CREATED, "Permiso creado", permission);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionResponseDTO>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePermissionDTO dto) {
        PermissionResponseDTO updated = permissionService.updatePermission(id, dto);
        return responseService.success(HttpStatus.OK, "Permiso actualizado", updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionResponseDTO>> getById(@PathVariable UUID id) {
        PermissionResponseDTO permission = permissionService.getPermissionById(id);
        return responseService.success(HttpStatus.OK, "Permiso encontrado", permission);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionResponseDTO>>> getAll() {
        List<PermissionResponseDTO> permissions = permissionService.getAllPermissions();
        return responseService.success(HttpStatus.OK, "Lista de permisos", permissions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        permissionService.deletePermission(id);
        return responseService.success(HttpStatus.NO_CONTENT, "Permiso eliminado");
    }
}
