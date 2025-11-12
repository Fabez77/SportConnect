package com.sportconnect.user.application.service;

import com.sportconnect.authorization.role.domain.model.Role;
import com.sportconnect.authorization.role.domain.repository.RoleRepository;
import com.sportconnect.user.api.dto.*;
import com.sportconnect.user.api.mapper.UserDtoMapper;
import com.sportconnect.user.application.service.impl.UserServiceImpl;
import com.sportconnect.user.domain.model.User;
import com.sportconnect.user.domain.repository.UserRepository;
import com.sportconnect.shared.datatable.dto.DataTableRequest;
import com.sportconnect.shared.datatable.dto.DataTableResponse;
import com.sportconnect.shared.datatable.service.DataTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserDtoMapper mapper;

    @Mock
    private DataTableService dataTableService;

    @InjectMocks
    private UserServiceImpl service;

    private User user;
    private UserResponseDTO responseDTO;
    private Role role;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        role = Role.builder().id(roleId).name("ADMIN").build();

        user = User.builder()
                .id(userId)
                .username("fabio")
                .email("fabio@test.com")
                .dni("12345678")
                .password("hashed")
                .roles(Set.of(role))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .build();

        responseDTO = UserResponseDTO.builder()
                .id(userId)
                .username("fabio")
                .email("fabio@test.com")
                .dni("12345678")
                .roles(Set.of("ADMIN"))
                .build();
    }

    @Test
    void createUser_shouldReturnResponse() {
        CreateUserDTO dto = CreateUserDTO.builder()
                .username("fabio")
                .email("fabio@test.com")
                .dni("12345678")
                .password("secret")
                .build();

        when(userRepository.findByUsername(dto.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.existsByDni(dto.getDni())).thenReturn(false);
        when(mapper.toDomain(dto)).thenReturn(user);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("hashed");
        when(userRepository.save(user)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(responseDTO);

        UserResponseDTO result = service.createUser(dto);

        assertThat(result.getUsername()).isEqualTo("fabio");
        assertThat(result.getRoles()).containsExactly("ADMIN");
        verify(mapper).toDomain(dto);
        verify(mapper).toResponse(user);
    }

    @Test
    void createUser_shouldThrowExceptionWhenUsernameExists() {
        CreateUserDTO dto = CreateUserDTO.builder()
                .username("fabio")
                .email("fabio@test.com")
                .dni("12345678")
                .password("secret")
                .build();

        when(userRepository.findByUsername(dto.getUsername())).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> service.createUser(dto));
    }

    @Test
    void updateUser_shouldUpdateAndReturnResponse() {
        UpdateUserDTO dto = UpdateUserDTO.builder()
                .email("new@test.com")
                .dni("87654321")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.existsByDni(dto.getDni())).thenReturn(false);

        doAnswer(invocation -> {
            UpdateUserDTO d = invocation.getArgument(0);
            User u = invocation.getArgument(1);
            u.setEmail(d.getEmail());
            u.setDni(d.getDni());
            return null;
        }).when(mapper).updateDomain(eq(dto), eq(user));

        when(userRepository.save(user)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(responseDTO);

        UserResponseDTO result = service.updateUser(user.getId(), dto);

        assertThat(result.getUsername()).isEqualTo("fabio");
        verify(mapper).updateDomain(dto, user);
        verify(mapper).toResponse(user);
        verify(userRepository).save(user);
    }

    @Test
    void getUserById_shouldReturnResponse() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mapper.toResponse(user)).thenReturn(responseDTO);

        UserResponseDTO result = service.getUserById(user.getId());

        assertThat(result.getUsername()).isEqualTo("fabio");
        verify(mapper).toResponse(user);
    }

    @Test
    void getUserById_shouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getUserById(id));
    }

    @Test
    void getUsers_shouldReturnPagedResponse() {
        DataTableRequest request = DataTableRequest.builder()
                .page(0).size(10).sortBy("username").direction("ASC")
                .filters(Map.of("active", "true"))
                .search("fabio")
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("username").ascending());
        Page<User> page = new PageImpl<>(List.of(user), pageable, 1);

        when(dataTableService.buildPageable(request)).thenReturn(pageable);
        when(userRepository.findAll(request.getFilters(), request.getSearch(), pageable)).thenReturn(page);

        // ✅ Usamos toResponse para cada User
        when(mapper.toResponse(user)).thenReturn(responseDTO);

        DataTableResponse<UserResponseDTO> expectedResponse = DataTableResponse.<UserResponseDTO>builder()
                .data(List.of(responseDTO))
                .totalElements(1)
                .totalPages(1)
                .currentPage(0)
                .build();

        when(dataTableService.buildResponse(ArgumentMatchers.<Page<UserResponseDTO>>any()))
        .thenReturn(expectedResponse);


        DataTableResponse<UserResponseDTO> result = service.getUsers(request);

        assertThat(result.getData()).hasSize(1);
        assertThat(result.getData().get(0).getUsername()).isEqualTo("fabio");
        verify(mapper).toResponse(user); // ✅ no uses toResponseList
    }

    @Test
    void deleteUser_shouldCallRepository() {
        UUID id = UUID.randomUUID();

        service.deleteUser(id);

        verify(userRepository).deleteById(id);
    }

    @Test
    void assignRoles_shouldUpdateUserRoles() {
        UUID userId = user.getId();
        List<UUID> roleIds = List.of(role.getId());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findAllById(roleIds)).thenReturn(List.of(role));

        service.assignRoles(userId, roleIds);

        assertThat(user.getRoles()).containsExactly(role);
        verify(userRepository).save(user);
    }

    @Test
    void assignRoles_shouldThrowExceptionWhenMissingRoles() {
        UUID userId = user.getId();
        List<UUID> roleIds = List.of(role.getId(), UUID.randomUUID());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findAllById(roleIds)).thenReturn(List.of(role)); // falta uno

        assertThrows(IllegalArgumentException.class, () -> service.assignRoles(userId, roleIds));
    }

    @Test
    void getUserRolesIds_shouldReturnRoleIds() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserRoleResponse result = service.getUserRolesIds(user.getId());

        assertThat(result.getRoles()).containsExactly(role.getId());
    }
}
