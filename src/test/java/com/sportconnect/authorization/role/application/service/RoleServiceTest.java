package com.sportconnect.authorization.role.application.service;

import com.sportconnect.authorization.permission.domain.model.Permission;
import com.sportconnect.authorization.permission.domain.repository.PermissionRepository;
import com.sportconnect.authorization.role.api.dto.*;
import com.sportconnect.authorization.role.api.mapper.RoleDtoMapper;
import com.sportconnect.authorization.role.application.service.impl.RoleServiceImpl;
import com.sportconnect.authorization.role.domain.model.Role;
import com.sportconnect.authorization.role.domain.repository.RoleRepository;
import com.sportconnect.shared.datatable.dto.DataTableRequest;
import com.sportconnect.shared.datatable.dto.DataTableResponse;
import com.sportconnect.shared.datatable.service.DataTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository repository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private RoleDtoMapper mapper;

    @Mock
    private DataTableService dataTableService;

    @InjectMocks
    private RoleServiceImpl service;

    private Role role;
    private RoleResponseDTO responseDTO;
    private Permission perm1;
    private Permission perm2;

    @BeforeEach
    void setUp() {
        UUID roleId = UUID.randomUUID();
        UUID permId1 = UUID.randomUUID();
        UUID permId2 = UUID.randomUUID();

        perm1 = Permission.builder().id(permId1).name("VIEW_USER").build();
        perm2 = Permission.builder().id(permId2).name("EDIT_USER").build();

        role = Role.builder()
                .id(roleId)
                .name("ADMIN")
                .description("Administrador")
                .permissions(Set.of(perm1, perm2))
                .build();

        responseDTO = RoleResponseDTO.builder()
                .id(roleId)
                .name("ADMIN")
                .description("Administrador")
                .permissions(Set.of("VIEW_USER", "EDIT_USER"))
                .build();
    }

    @Test
    void createRole_shouldReturnResponse() {
        CreateRoleDTO dto = CreateRoleDTO.builder()
                .name("ADMIN")
                .description("Administrador")
                .build();

        // ✅ Usamos toDomain y toResponse
        when(mapper.toDomain(dto)).thenReturn(role);
        when(repository.save(role)).thenReturn(role);
        when(mapper.toResponse(role)).thenReturn(responseDTO);

        RoleResponseDTO result = service.createRole(dto);

        assertThat(result.getName()).isEqualTo("ADMIN");
        assertThat(result.getDescription()).isEqualTo("Administrador");
        verify(mapper).toDomain(dto);
        verify(mapper).toResponse(role);
        verify(repository).save(role);
    }

    @Test
    void updateRole_shouldUpdateNameAndDescription() {
        UpdateRoleDTO dto = UpdateRoleDTO.builder()
                .name("MANAGER")
                .description("Gestor de usuarios")
                .build();

        Role existing = Role.builder()
                .id(role.getId())
                .name("OLD_NAME")
                .description("Antiguo nombre")
                .permissions(new HashSet<>())
                .build();

        Role updated = Role.builder()
                .id(role.getId())
                .name("MANAGER")
                .description("Gestor de usuarios")
                .permissions(existing.getPermissions())
                .build();

        RoleResponseDTO response = RoleResponseDTO.builder()
                .id(role.getId())
                .name("MANAGER")
                .description("Gestor de usuarios")
                .permissions(Set.of("VIEW_USER", "EDIT_USER"))
                .build();

        when(repository.findById(role.getId())).thenReturn(Optional.of(existing));

        // ✅ Usamos updateDomain
        doAnswer(invocation -> {
            UpdateRoleDTO d = invocation.getArgument(0);
            Role r = invocation.getArgument(1);
            r.setName(d.getName());
            r.setDescription(d.getDescription());
            return null;
        }).when(mapper).updateDomain(eq(dto), eq(existing));

        when(repository.save(existing)).thenReturn(updated);
        when(mapper.toResponse(updated)).thenReturn(response);

        RoleResponseDTO result = service.updateRole(role.getId(), dto);

        assertThat(result.getName()).isEqualTo("MANAGER");
        assertThat(result.getDescription()).isEqualTo("Gestor de usuarios");
        assertThat(result.getPermissions()).containsExactlyInAnyOrder("VIEW_USER", "EDIT_USER");
        verify(mapper).updateDomain(dto, existing);
        verify(mapper).toResponse(updated);
    }

    @Test
    void getRoles_shouldReturnPagedResponse() {
        DataTableRequest request = DataTableRequest.builder()
                .page(0).size(10).sortBy("name").direction("ASC")
                .filters(Map.of("name", "ADMIN"))
                .search("ADMIN")
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Role> page = new PageImpl<>(List.of(role), pageable, 1);

        when(dataTableService.buildPageable(request)).thenReturn(pageable);
        when(repository.findAll(request.getFilters(), request.getSearch(), pageable)).thenReturn(page);

        // ✅ Usamos toResponse para cada Role
        when(mapper.toResponse(role)).thenReturn(responseDTO);

        DataTableResponse<RoleResponseDTO> expectedResponse =
                DataTableResponse.<RoleResponseDTO>builder()
                        .data(List.of(responseDTO))
                        .totalElements(1)
                        .totalPages(1)
                        .currentPage(0)
                        .build();

        when(dataTableService.buildResponse(ArgumentMatchers.<Page<RoleResponseDTO>>any()))
        .thenReturn(expectedResponse);



        DataTableResponse<RoleResponseDTO> result = service.getRoles(request);

        assertThat(result.getData()).hasSize(1);
        assertThat(result.getData().get(0).getName()).isEqualTo("ADMIN");
        verify(mapper).toResponse(role);
    }

    @Test
    void getRoleById_shouldReturnResponse() {
        when(repository.findById(role.getId())).thenReturn(Optional.of(role));
        when(mapper.toResponse(role)).thenReturn(responseDTO);

        RoleResponseDTO result = service.getRoleById(role.getId());

        assertThat(result.getName()).isEqualTo("ADMIN");
        verify(mapper).toResponse(role);
    }

    @Test
    void getRoleById_shouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getRoleById(id));
    }

    @Test
    void deleteRole_shouldCallRepository() {
        UUID id = UUID.randomUUID();

        service.deleteRole(id);

        verify(repository).deleteById(id);
    }

    @Test
    void assignPermissions_shouldUpdateRolePermissions() {
        UUID roleId = role.getId();
        List<UUID> permissionIds = List.of(perm1.getId(), perm2.getId());

        when(repository.findById(roleId)).thenReturn(Optional.of(role));
        when(permissionRepository.findAllById(permissionIds)).thenReturn(List.of(perm1, perm2));

        service.assignPermissions(roleId, permissionIds);

        assertThat(role.getPermissions()).containsExactlyInAnyOrder(perm1, perm2);
        verify(repository).save(role);
    }

    @Test
    void assignPermissions_shouldThrowExceptionWhenMissingPermissions() {
        UUID roleId = role.getId();
        List<UUID> permissionIds = List.of(perm1.getId(), perm2.getId());

        when(repository.findById(roleId)).thenReturn(Optional.of(role));
        when(permissionRepository.findAllById(permissionIds)).thenReturn(List.of(perm1)); // falta uno

        assertThrows(IllegalArgumentException.class, () -> service.assignPermissions(roleId, permissionIds));
    }

    @Test
    void getRolePermissionIds_shouldReturnPermissionIds() {
        when(repository.findById(role.getId())).thenReturn(Optional.of(role));

        RolePermissionsResponse result = service.getRolePermissionIds(role.getId());

        assertThat(result.getPermissions()).containsExactlyInAnyOrder(perm1.getId(), perm2.getId());
    }
}
