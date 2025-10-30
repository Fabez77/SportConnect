package com.sportconnect.authorization.role.api.controller;

import com.sportconnect.authorization.role.api.dto.*;
import com.sportconnect.authorization.role.application.service.RoleService;
import com.sportconnect.shared.apiresponse.dto.ApiResponse;
import com.sportconnect.shared.apiresponse.service.ApiResponseService;
import com.sportconnect.shared.datatable.dto.DataTableRequest;
import com.sportconnect.shared.datatable.dto.DataTableResponse;

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

    private final RoleService roleService;
    private final ApiResponseService responseService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponseDTO>> create(@Valid @RequestBody CreateRoleDTO dto) {
        RoleResponseDTO role = roleService.createRole(dto);
        return responseService.success(HttpStatus.CREATED, "Rol creado", role);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateRoleDTO dto) {
        RoleResponseDTO updated = roleService.updateRole(id, dto);
        return responseService.success(HttpStatus.OK, "Rol actualizado", updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> getById(@PathVariable UUID id) {
        RoleResponseDTO role = roleService.getRoleById(id);
        return responseService.success(HttpStatus.OK, "Rol encontrado", role);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DataTableResponse<RoleResponseDTO>>> getRoles(DataTableRequest request) {
        DataTableResponse<RoleResponseDTO> roles = roleService.getRoles(request);
        return responseService.success(HttpStatus.OK, "Lista de roles paginada", roles);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        roleService.deleteRole(id);
        return responseService.success(HttpStatus.NO_CONTENT, "Rol eliminado");
    }

    @GetMapping("/{id}/permissions")
    public ResponseEntity<ApiResponse<RolePermissionsResponse>> getRolePermissions(@PathVariable UUID id) {
        RolePermissionsResponse response = roleService.getRolePermissionIds(id);
        return responseService.success(HttpStatus.OK, "Lista de permisos", response);
    }

    @PutMapping("/{roleId}/permissions")
    public ResponseEntity<ApiResponse<Void>> assignPermissions(
            @PathVariable UUID roleId,
            @RequestBody AssignPermissionsRequest request) {

        roleService.assignPermissions(roleId, request.permissionIds());

        return responseService.success(
                HttpStatus.NO_CONTENT,
                "Permisos asignados correctamente");
    }
}
