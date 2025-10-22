package com.sportconnect.user.application.service;

import com.sportconnect.user.api.dto.*;
import com.sportconnect.user.api.mapper.UserDtoMapper;
import com.sportconnect.user.domain.model.User;
import com.sportconnect.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDtoMapper mapper;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User user;
    private UserResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder()
                .id(userId)
                .username("fabio")
                .email("fabio@mail.com")
                .dni("12345678")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .roles(new HashSet<>())
                .build();

        responseDTO = UserResponseDTO.builder()
                .id(userId)
                .username("fabio")
                .email("fabio@mail.com")
                .dni("12345678")
                .active(true)
                .roles(Set.of("ADMIN"))
                .build();
    }

    @Test
    void createUser_shouldReturnUserResponseDTO() {
        CreateUserDTO dto = CreateUserDTO.builder()
                .username("fabio")
                .email("fabio@mail.com")
                .dni("12345678")
                .build();

        when(mapper.toDomain(dto)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.createUser(dto);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(dto.getUsername());
        assertThat(result.getEmail()).isEqualTo(dto.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_shouldReturnUpdatedUserResponseDTO() {
        UpdateUserDTO dto = UpdateUserDTO.builder()
                .username("fabio_updated")
                .email("fabio_updated@mail.com")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doAnswer(invocation -> {
            UpdateUserDTO d = invocation.getArgument(0);
            User u = invocation.getArgument(1);
            u.setUsername(d.getUsername());
            u.setEmail(d.getEmail());
            return null;
        }).when(mapper).updateDomain(eq(dto), any(User.class));
        when(userRepository.save(user)).thenReturn(user);

        UserResponseDTO updatedResponse = UserResponseDTO.builder()
                .id(userId)
                .username(dto.getUsername())
                .email(dto.getEmail())
                .dni(user.getDni())
                .active(true)
                .roles(Set.of("ADMIN"))
                .build();

        when(mapper.toResponse(user)).thenReturn(updatedResponse);

        UserResponseDTO result = userService.updateUser(userId, dto);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(dto.getUsername());
        assertThat(result.getEmail()).isEqualTo(dto.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void getUserById_shouldReturnUserResponseDTO() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.toResponse(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.getUserById(userId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getUsername()).isEqualTo("fabio");
        verify(userRepository).findById(userId);
    }

    @Test
    void listUsers_shouldReturnListOfUserResponseDTO() {
        List<User> users = List.of(user);
        List<UserResponseDTO> responseList = List.of(responseDTO);

        when(userRepository.findAll()).thenReturn(users);
        when(mapper.toResponseList(users)).thenReturn(responseList);

        List<UserResponseDTO> result = userService.listUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("fabio");
        verify(userRepository).findAll();
    }

    @Test
    void deleteUser_shouldCallRepositoryDelete() {
        doNothing().when(userRepository).delete(userId);

        userService.deleteUser(userId);

        verify(userRepository).delete(userId);
    }
}
