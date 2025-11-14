package com.sportconnect.authorization.permission.api.controller;

import com.sportconnect.authorization.permission.api.dto.*;
import com.sportconnect.authorization.permission.application.service.PermissionService;
import com.sportconnect.shared.apiresponse.dto.ApiResponse;
import com.sportconnect.shared.apiresponse.service.ApiResponseService;
import com.sportconnect.shared.datatable.dto.DataTableRequest;
import com.sportconnect.shared.datatable.dto.DataTableResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;
    private final ApiResponseService responseService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<PermissionResponseDTO>> create(@Valid @RequestBody CreatePermissionDTO dto) {
        PermissionResponseDTO permission = permissionService.createPermission(dto);
        return responseService.success(HttpStatus.CREATED, "Permiso creado", permission);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionResponseDTO>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePermissionDTO dto) {
        PermissionResponseDTO updated = permissionService.updatePermission(id, dto);
        return responseService.success(HttpStatus.OK, "Permiso actualizado", updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionResponseDTO>> getById(@PathVariable UUID id) {
        PermissionResponseDTO permission = permissionService.getPermissionById(id);
        return responseService.success(HttpStatus.OK, "Permiso encontrado", permission);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<DataTableResponse<PermissionResponseDTO>>> getPermissions(
            DataTableRequest request) {
        DataTableResponse<PermissionResponseDTO> permissions = permissionService.getPermissions(request);
        return responseService.success(HttpStatus.OK, "Lista de permisos paginada", permissions);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        permissionService.deletePermission(id);
        return responseService.success(HttpStatus.NO_CONTENT, "Permiso eliminado");
    }
}
