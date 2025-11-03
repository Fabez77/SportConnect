package com.sportconnect.authorization.permission.application.service;

import com.sportconnect.authorization.permission.api.dto.*;
import com.sportconnect.authorization.permission.api.mapper.PermissionDtoMapper;
import com.sportconnect.authorization.permission.application.service.impl.PermissionServiceImpl;
import com.sportconnect.authorization.permission.domain.model.Permission;
import com.sportconnect.authorization.permission.domain.repository.PermissionRepository;
import com.sportconnect.shared.datatable.dto.DataTableRequest;
import com.sportconnect.shared.datatable.dto.DataTableResponse;
import com.sportconnect.shared.datatable.service.DataTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PermissionServiceImplTest {

    @Mock
    private PermissionRepository repository;

    @Mock
    private PermissionDtoMapper dtoMapper;

    @Mock
    private DataTableService dataTableService;

    @InjectMocks
    private PermissionServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPermission_shouldReturnResponseDTO() {
        CreatePermissionDTO dto = new CreatePermissionDTO("CREATE_USER", "Permite crear usuarios");
        Permission domain = new Permission(UUID.randomUUID(), "CREATE_USER", "Permite crear usuarios");
        PermissionResponseDTO response = new PermissionResponseDTO(domain.getId(), domain.getName(), domain.getDescription());

        when(dtoMapper.toDomain(dto)).thenReturn(domain);
        when(repository.save(domain)).thenReturn(domain);
        when(dtoMapper.toResponse(domain)).thenReturn(response);

        PermissionResponseDTO result = service.createPermission(dto);

        assertThat(result.getName()).isEqualTo("CREATE_USER");
        verify(repository).save(domain);
    }

    @Test
    void updatePermission_shouldUpdateAndReturnResponseDTO() {
        UUID id = UUID.randomUUID();
        UpdatePermissionDTO dto = new UpdatePermissionDTO("DELETE_USER", "Permite eliminar usuarios");
        Permission existing = new Permission(id, "CREATE_USER", "Permite crear usuarios");
        PermissionResponseDTO response = new PermissionResponseDTO(id, "DELETE_USER", "Permite eliminar usuarios");

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        doAnswer(invocation -> {
            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            return null;
        }).when(dtoMapper).updateDomain(dto, existing);
        when(repository.save(existing)).thenReturn(existing);
        when(dtoMapper.toResponse(existing)).thenReturn(response);

        PermissionResponseDTO result = service.updatePermission(id, dto);

        assertThat(result.getName()).isEqualTo("DELETE_USER");
        verify(repository).save(existing);
    }

    @Test
    void getPermissions_shouldReturnDataTableResponse() {
        DataTableRequest request = new DataTableRequest();
        Pageable pageable = PageRequest.of(0, 10);
        Permission domain = new Permission(UUID.randomUUID(), "VIEW_USER", "Permite ver usuarios");
        PermissionResponseDTO dto = new PermissionResponseDTO(domain.getId(), domain.getName(), domain.getDescription());

        Page<Permission> page = new PageImpl<>(List.of(domain), pageable, 1);
        Page<PermissionResponseDTO> dtoPage = new PageImpl<>(List.of(dto), pageable, 1);
        DataTableResponse<PermissionResponseDTO> expectedResponse = DataTableResponse.<PermissionResponseDTO>builder()
                .data(List.of(dto))
                .totalElements(1L)
                .totalPages(1)
                .currentPage(0)
                .build();

        when(dataTableService.buildPageable(request)).thenReturn(pageable);
        // simulamos que el repo devuelve un Page<Permission>
        when(((PermissionRepository) repository).findAll(any(), eq(pageable))).thenReturn(page);
        when(dtoMapper.toResponse(domain)).thenReturn(dto);
        when(dataTableService.buildResponse(dtoPage)).thenReturn(expectedResponse);

        DataTableResponse<PermissionResponseDTO> result = service.getPermissions(request);

        assertThat(result).isEqualTo(expectedResponse);
        verify(repository).findAll(any(), eq(pageable));
    }

    @Test
    void getPermissionById_shouldReturnResponseDTO() {
        UUID id = UUID.randomUUID();
        Permission domain = new Permission(id, "VIEW_USER", "Permite ver usuarios");
        PermissionResponseDTO dto = new PermissionResponseDTO(id, "VIEW_USER", "Permite ver usuarios");

        when(repository.findById(id)).thenReturn(Optional.of(domain));
        when(dtoMapper.toResponse(domain)).thenReturn(dto);

        PermissionResponseDTO result = service.getPermissionById(id);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void deletePermission_shouldCallRepository() {
        UUID id = UUID.randomUUID();

        service.deletePermission(id);

        verify(repository).deleteById(id);
    }
}
