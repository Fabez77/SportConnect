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
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PermissionServiceImplTest {

    @Mock
    private PermissionRepository repository;

    @Mock
    private PermissionDtoMapper mapper;

    @Mock
    private DataTableService dataTableService;

    @InjectMocks
    private PermissionServiceImpl service;

    private Permission permission;
    private PermissionResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        permission = Permission.builder()
                .id(UUID.randomUUID())
                .name("READ_USER")
                .description("Permite leer usuarios")
                .category("USER_MANAGEMENT")
                .build();

        responseDTO = PermissionResponseDTO.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .category(permission.getCategory())
                .build();
    }

    @Test
    void testCreatePermission() {
        CreatePermissionDTO dto = CreatePermissionDTO.builder()
                .name("READ_USER")
                .description("Permite leer usuarios")
                .category("USER_MANAGEMENT")
                .build();

        when(mapper.toDomain(dto)).thenReturn(permission);
        when(repository.save(permission)).thenReturn(permission);
        when(mapper.toResponse(permission)).thenReturn(responseDTO);

        PermissionResponseDTO result = service.createPermission(dto);

        assertEquals("READ_USER", result.getName());
        assertEquals("USER_MANAGEMENT", result.getCategory());
        verify(mapper).toDomain(dto);
        verify(mapper).toResponse(permission);
        verify(repository).save(permission);
    }

    @Test
    void testUpdatePermission() {
        UUID id = permission.getId();
        UpdatePermissionDTO dto = UpdatePermissionDTO.builder()
                .name("WRITE_USER")
                .description("Permite escribir usuarios")
                .category("USER_MANAGEMENT")
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(permission));

        doAnswer(invocation -> {
            UpdatePermissionDTO d = invocation.getArgument(0);
            Permission p = invocation.getArgument(1);
            p.setName(d.getName());
            p.setDescription(d.getDescription());
            p.setCategory(d.getCategory());
            return null;
        }).when(mapper).updateDomain(dto, permission);

        when(repository.save(permission)).thenReturn(permission);

        when(mapper.toResponse(permission)).thenReturn(
                PermissionResponseDTO.builder()
                        .id(id)
                        .name(dto.getName())
                        .description(dto.getDescription())
                        .category(dto.getCategory())
                        .build()
        );

        PermissionResponseDTO result = service.updatePermission(id, dto);

        assertEquals("WRITE_USER", result.getName());
        assertEquals("USER_MANAGEMENT", result.getCategory());
        verify(mapper).updateDomain(dto, permission);
        verify(mapper).toResponse(permission);
        verify(repository).save(permission);
    }

    @Test
    void testGetPermissions() {
        Map<String, String> filters = Map.of("category", "USER_MANAGEMENT");

        DataTableRequest request = DataTableRequest.builder()
                .page(0).size(10).sortBy("name").direction("ASC")
                .search("READ").filters(filters).build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Permission> page = new PageImpl<>(List.of(permission), pageable, 1);

        when(dataTableService.buildPageable(request)).thenReturn(pageable);
        when(repository.findAll(filters, "READ", pageable)).thenReturn(page);
        when(mapper.toResponse(permission)).thenReturn(responseDTO);

        DataTableResponse<PermissionResponseDTO> expectedResponse = DataTableResponse.<PermissionResponseDTO>builder()
                .data(List.of(responseDTO))
                .totalElements(1)
                .totalPages(1)
                .currentPage(0)
                .build();

        when(dataTableService.buildResponse(ArgumentMatchers.<Page<PermissionResponseDTO>>any()))
        .thenReturn(expectedResponse);



        DataTableResponse<PermissionResponseDTO> result = service.getPermissions(request);

        assertEquals(1, result.getData().size());
        assertEquals("READ_USER", result.getData().get(0).getName());
        assertEquals("USER_MANAGEMENT", result.getData().get(0).getCategory());
        verify(mapper).toResponse(permission);
    }

    @Test
    void testGetPermissionById_found() {
        UUID id = permission.getId();
        when(repository.findById(id)).thenReturn(Optional.of(permission));
        when(mapper.toResponse(permission)).thenReturn(responseDTO);

        PermissionResponseDTO result = service.getPermissionById(id);

        assertEquals("READ_USER", result.getName());
        assertEquals("USER_MANAGEMENT", result.getCategory());
        verify(mapper).toResponse(permission);
    }

    @Test
    void testGetPermissionById_notFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getPermissionById(id));
    }

    @Test
    void testDeletePermission() {
        UUID id = permission.getId();
        doNothing().when(repository).deleteById(id);

        service.deletePermission(id);

        verify(repository).deleteById(id);
    }
}
