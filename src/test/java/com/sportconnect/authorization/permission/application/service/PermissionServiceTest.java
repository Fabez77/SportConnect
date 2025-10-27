package com.sportconnect.authorization.permission.application.service;

import com.sportconnect.authorization.permission.api.dto.CreatePermissionDTO;
import com.sportconnect.authorization.permission.api.dto.PermissionResponseDTO;
import com.sportconnect.authorization.permission.api.dto.UpdatePermissionDTO;
import com.sportconnect.authorization.permission.domain.model.Permission;
import com.sportconnect.authorization.permission.domain.repository.PermissionRepository;
import com.sportconnect.authorization.permission.infrastructure.mapper.PermissionEntityMapper;
import com.sportconnect.authorization.permission.api.mapper.PermissionDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private PermissionEntityMapper entityMapper;

    @Mock
    private PermissionDtoMapper dtoMapper;

    @InjectMocks
    private PermissionServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPermission_shouldReturnResponseDTO() {
        // Arrange
        CreatePermissionDTO dto = new CreatePermissionDTO("CREATE_USER", "Permite crear usuarios");
        Permission domain = new Permission(UUID.randomUUID(), "CREATE_USER", "Permite crear usuarios");
        PermissionResponseDTO response = new PermissionResponseDTO(domain.getId(), domain.getName(), domain.getDescription());

        when(dtoMapper.toDomain(dto)).thenReturn(domain);
        when(permissionRepository.save(domain)).thenReturn(domain);
        when(dtoMapper.toResponse(domain)).thenReturn(response);

        // Act
        PermissionResponseDTO result = service.createPermission(dto);

        // Assert
        assertThat(result.getName()).isEqualTo("CREATE_USER");
        verify(permissionRepository, times(1)).save(domain);
    }

    @Test
    void updatePermission_shouldUpdateAndReturnResponseDTO() {
        // Arrange
        UUID id = UUID.randomUUID();
        UpdatePermissionDTO dto = new UpdatePermissionDTO("DELETE_USER", "Permite eliminar usuarios");
        Permission existing = new Permission(id, "CREATE_USER", "Permite crear usuarios");
        Permission updated = new Permission(id, "DELETE_USER", "Permite eliminar usuarios");
        PermissionResponseDTO response = new PermissionResponseDTO(id, "DELETE_USER", "Permite eliminar usuarios");

        when(permissionRepository.findById(id)).thenReturn(Optional.of(existing));
        doAnswer(invocation -> {
            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            return null;
        }).when(dtoMapper).updateDomain(dto, existing);
        when(permissionRepository.save(existing)).thenReturn(updated);
        when(dtoMapper.toResponse(updated)).thenReturn(response);

        // Act
        PermissionResponseDTO result = service.updatePermission(id, dto);

        // Assert
        assertThat(result.getName()).isEqualTo("DELETE_USER");
        verify(permissionRepository, times(1)).save(existing);
    }

    @Test
    void getAllPermissions_shouldReturnList() {
        // Arrange
        Permission p1 = new Permission(UUID.randomUUID(), "VIEW_USER", "Permite ver usuarios");
        Permission p2 = new Permission(UUID.randomUUID(), "EDIT_USER", "Permite editar usuarios");
        List<Permission> domainList = Arrays.asList(p1, p2);
        List<PermissionResponseDTO> responseList = Arrays.asList(
                new PermissionResponseDTO(p1.getId(), p1.getName(), p1.getDescription()),
                new PermissionResponseDTO(p2.getId(), p2.getName(), p2.getDescription())
        );

        when(permissionRepository.findAll()).thenReturn(domainList);
        when(dtoMapper.toResponseList(domainList)).thenReturn(responseList);

        // Act
        List<PermissionResponseDTO> result = service.getAllPermissions();

        // Assert
        assertThat(result).hasSize(2);
        verify(permissionRepository, times(1)).findAll();
    }
}
