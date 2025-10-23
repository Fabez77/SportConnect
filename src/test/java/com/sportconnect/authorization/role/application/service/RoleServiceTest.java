package com.sportconnect.authorization.role.application.service;

import com.sportconnect.authorization.permission.domain.model.Permission;
import com.sportconnect.authorization.permission.domain.repository.PermissionRepository;
import com.sportconnect.authorization.role.api.dto.CreateRoleDTO;
import com.sportconnect.authorization.role.api.dto.RoleResponseDTO;
import com.sportconnect.authorization.role.api.dto.UpdateRoleDTO;
import com.sportconnect.authorization.role.api.mapper.RoleDtoMapper;
import com.sportconnect.authorization.role.application.service.impl.RoleServiceImpl;
import com.sportconnect.authorization.role.domain.model.Role;
import com.sportconnect.authorization.role.domain.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository repository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private RoleDtoMapper mapper;

    @InjectMocks
    private RoleServiceImpl service;

    private UUID roleId;
    private UUID permId1;
    private UUID permId2;
    private Permission perm1;
    private Permission perm2;

    @BeforeEach
    void setUp() {
        roleId = UUID.randomUUID();
        permId1 = UUID.randomUUID();
        permId2 = UUID.randomUUID();
        perm1 = Permission.builder().id(permId1).name("VIEW_USER").build();
        perm2 = Permission.builder().id(permId2).name("EDIT_USER").build();
    }

    @Test
    void createRole_shouldResolvePermissionsAndReturnResponse() {
        CreateRoleDTO dto = CreateRoleDTO.builder()
                .name("MODERATOR")
                .description(null)
                .permissions(Set.of(permId1, permId2))
                .build();

        Role mapped = Role.builder()
                .name("MODERATOR")
                .description(null)
                .permissions(null)
                .build();

        Role saved = Role.builder()
                .id(roleId)
                .name("MODERATOR")
                .description(null)
                .permissions(Set.of(perm1, perm2))
                .build();

        RoleResponseDTO response = RoleResponseDTO.builder()
                .id(roleId)
                .name("MODERATOR")
                .description(null)
                .permissions(Set.of("VIEW_USER", "EDIT_USER"))
                .build();

        when(mapper.toDomain(dto)).thenReturn(mapped);
        when(permissionRepository.findById(permId1)).thenReturn(Optional.of(perm1));
        when(permissionRepository.findById(permId2)).thenReturn(Optional.of(perm2));
        when(repository.save(any(Role.class))).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        RoleResponseDTO result = service.createRole(dto);

        assertThat(result.getId()).isEqualTo(roleId);
        assertThat(result.getName()).isEqualTo("MODERATOR");
        assertThat(result.getPermissions()).containsExactlyInAnyOrder("VIEW_USER", "EDIT_USER");
    }

    @Test
    void updateRole_shouldUpdateNameAndPermissions() {
        UpdateRoleDTO dto = UpdateRoleDTO.builder()
                .name("MANAGER")
                .description(null)
                .permissions(Set.of(permId1))
                .build();

        Role existing = Role.builder()
                .id(roleId)
                .name("OLD_NAME")
                .description(null)
                .permissions(new HashSet<>())
                .build();

        Role updated = Role.builder()
                .id(roleId)
                .name("MANAGER")
                .description(null)
                .permissions(Set.of(perm1))
                .build();

        RoleResponseDTO response = RoleResponseDTO.builder()
                .id(roleId)
                .name("MANAGER")
                .description(null)
                .permissions(Set.of("VIEW_USER"))
                .build();

        when(repository.findById(roleId)).thenReturn(Optional.of(existing));
        doAnswer(invocation -> {
            UpdateRoleDTO d = invocation.getArgument(0);
            Role r = invocation.getArgument(1);
            r.setName(d.getName());
            return null;
        }).when(mapper).updateDomain(eq(dto), eq(existing));
        when(permissionRepository.findById(permId1)).thenReturn(Optional.of(perm1));
        when(repository.save(existing)).thenReturn(updated);
        when(mapper.toResponse(updated)).thenReturn(response);

        RoleResponseDTO result = service.updateRole(roleId, dto);

        assertThat(result.getName()).isEqualTo("MANAGER");
        assertThat(result.getPermissions()).containsExactly("VIEW_USER");
    }

    @Test
    void getAllRoles_shouldReturnMappedList() {
        Role role1 = Role.builder()
                .id(UUID.randomUUID())
                .name("ADMIN")
                .description(null)
                .permissions(Set.of(perm1))
                .build();

        Role role2 = Role.builder()
                .id(UUID.randomUUID())
                .name("COACH")
                .description(null)
                .permissions(Set.of(perm2))
                .build();

        RoleResponseDTO dto1 = RoleResponseDTO.builder()
                .id(role1.getId())
                .name("ADMIN")
                .description(null)
                .permissions(Set.of("VIEW_USER"))
                .build();

        RoleResponseDTO dto2 = RoleResponseDTO.builder()
                .id(role2.getId())
                .name("COACH")
                .description(null)
                .permissions(Set.of("EDIT_USER"))
                .build();

        when(repository.findAll()).thenReturn(List.of(role1, role2));
        when(mapper.toResponse(role1)).thenReturn(dto1);
        when(mapper.toResponse(role2)).thenReturn(dto2);

        List<RoleResponseDTO> result = service.getAllRoles();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(RoleResponseDTO::getName).containsExactlyInAnyOrder("ADMIN", "COACH");
    }

    @Test
    void getRoleById_shouldReturnMappedRole() {
        Role role = Role.builder()
                .id(roleId)
                .name("PLAYER")
                .description(null)
                .permissions(Set.of(perm1))
                .build();

        RoleResponseDTO dto = RoleResponseDTO.builder()
                .id(roleId)
                .name("PLAYER")
                .description(null)
                .permissions(Set.of("VIEW_USER"))
                .build();

        when(repository.findById(roleId)).thenReturn(Optional.of(role));
        when(mapper.toResponse(role)).thenReturn(dto);

        RoleResponseDTO result = service.getRoleById(roleId);

        assertThat(result.getName()).isEqualTo("PLAYER");
    }

    @Test
    void deleteRole_shouldCallRepository() {
        UUID id = UUID.randomUUID();

        service.deleteRole(id);

        verify(repository).deleteById(id);
    }
}
