package com.sportconnect.user.application.service;

import com.sportconnect.authorization.role.domain.model.Role;
import com.sportconnect.authorization.role.domain.repository.RoleRepository;
import com.sportconnect.user.api.dto.*;
import com.sportconnect.user.api.mapper.UserDtoMapper;
import com.sportconnect.user.domain.model.User;
import com.sportconnect.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserDtoMapper mapper;

    @InjectMocks private UserServiceImpl userService;

    @Test
    void createUser_success() {
        CreateUserDTO dto = new CreateUserDTO("fabio", "fabio@mail.com", "12345678", "secret");
        User user = User.builder().username("fabio").email("fabio@mail.com").dni("12345678").build();
        User saved = User.builder().id(UUID.randomUUID()).username("fabio").build();
        UserResponseDTO response = new UserResponseDTO(saved.getId(), "fabio", "fabio@mail.com", "12345678", true, Set.of());

        when(userRepository.findByUsername("fabio")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("fabio@mail.com")).thenReturn(Optional.empty());
        when(userRepository.existsByDni("12345678")).thenReturn(false);
        when(mapper.toDomain(dto)).thenReturn(user);
        when(passwordEncoder.encode("secret")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        UserResponseDTO result = userService.createUser(dto);

        assertEquals("fabio", result.getUsername());
        assertTrue(result.getRoles().isEmpty());
    }

    @Test
    void updateUser_success() {
        UUID id = UUID.randomUUID();
        UpdateUserDTO dto = new UpdateUserDTO("fabio", "fabio@mail.com", "12345678", true);

        User user = User.builder()
                .id(id)
                .username("old")
                .email("old@mail.com")
                .dni("oldDni")
                .active(true)
                .build();

        User updated = User.builder()
                .id(id)
                .username("fabio")
                .email("fabio@mail.com")
                .dni("12345678")
                .active(true)
                .build();

        UserResponseDTO response = new UserResponseDTO(id, "fabio", "fabio@mail.com", "12345678", true, Set.of("ADMIN"));

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("fabio@mail.com")).thenReturn(Optional.empty());
        when(userRepository.existsByDni("12345678")).thenReturn(false);

        doAnswer(invocation -> {
            UpdateUserDTO dtoArg = invocation.getArgument(0);
            User userArg = invocation.getArgument(1);
            userArg.setUsername(dtoArg.getUsername());
            userArg.setEmail(dtoArg.getEmail());
            userArg.setDni(dtoArg.getDni());
            userArg.setActive(dtoArg.getActive());
            return null;
        }).when(mapper).updateDomain(dto, user);

        when(userRepository.save(user)).thenReturn(updated);
        when(mapper.toResponse(updated)).thenReturn(response);

        UserResponseDTO result = userService.updateUser(id, dto);

        assertEquals("fabio", result.getUsername());
        assertEquals("fabio@mail.com", result.getEmail());
        assertEquals("12345678", result.getDni());
        assertTrue(result.getRoles().contains("ADMIN"));
    }

    @Test
    void getUserById_success() {
        UUID id = UUID.randomUUID();
        User user = User.builder().id(id).username("fabio").build();
        UserResponseDTO response = new UserResponseDTO(id, "fabio", "fabio@mail.com", "12345678", true, Set.of("ADMIN"));

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(mapper.toResponse(user)).thenReturn(response);

        UserResponseDTO result = userService.getUserById(id);

        assertEquals("fabio", result.getUsername());
        assertTrue(result.getRoles().contains("ADMIN"));
    }

    @Test
    void listUsers_success() {
        User user1 = User.builder().id(UUID.randomUUID()).username("fabio").build();
        User user2 = User.builder().id(UUID.randomUUID()).username("ana").build();
        List<User> users = List.of(user1, user2);
        List<UserResponseDTO> responses = List.of(
                new UserResponseDTO(user1.getId(), "fabio", "fabio@mail.com", "12345678", true, Set.of("ADMIN")),
                new UserResponseDTO(user2.getId(), "ana", "ana@mail.com", "87654321", true, Set.of("MODERATOR"))
        );

        when(userRepository.findAll()).thenReturn(users);
        when(mapper.toResponseList(users)).thenReturn(responses);

        List<UserResponseDTO> result = userService.listUsers();

        assertEquals(2, result.size());
        assertTrue(result.get(0).getRoles().contains("ADMIN"));
        assertTrue(result.get(1).getRoles().contains("MODERATOR"));
    }

    @Test
    void deleteUser_success() {
        UUID id = UUID.randomUUID();
        userService.deleteUser(id);
        verify(userRepository).delete(id);
    }

    @Test
    void assignRoles_success() {
        UUID userId = UUID.randomUUID();
        UUID roleId1 = UUID.randomUUID();
        UUID roleId2 = UUID.randomUUID();

        Role role1 = Role.builder().id(roleId1).name("ADMIN").build();
        Role role2 = Role.builder().id(roleId2).name("MODERATOR").build();
        User user = User.builder().id(userId).roles(new HashSet<>()).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findAllById(List.of(roleId1, roleId2))).thenReturn(List.of(role1, role2));

        userService.assignRoles(userId, List.of(roleId1, roleId2));

        verify(userRepository).save(user);
        assertEquals(Set.of(role1, role2), user.getRoles());
    }

    @Test
    void assignRoles_userNotFound_throwsException() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> userService.assignRoles(userId, List.of(UUID.randomUUID())));
    }

    @Test
    void assignRoles_someRolesMissing_throwsException() {
        UUID userId = UUID.randomUUID();
        UUID roleId1 = UUID.randomUUID();
        UUID roleId2 = UUID.randomUUID();

        Role role1 = Role.builder().id(roleId1).name("ADMIN").build();
        User user = User.builder().id(userId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findAllById(List.of(roleId1, roleId2))).thenReturn(List.of(role1)); // falta uno

        assertThrows(IllegalArgumentException.class,
            () -> userService.assignRoles(userId, List.of(roleId1, roleId2)));
    }
}
