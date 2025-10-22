package com.sportconnect.user.application.service;

import com.sportconnect.user.api.dto.CreateUserDTO;
import com.sportconnect.user.api.dto.UpdateUserDTO;
import com.sportconnect.user.api.dto.UserResponseDTO;
import com.sportconnect.user.domain.model.User;
import com.sportconnect.user.domain.repository.UserRepository;
import com.sportconnect.user.api.mapper.UserDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDtoMapper mapper;

    @InjectMocks
    private UserService userService;

    private CreateUserDTO createDto;
    private UpdateUserDTO updateDto;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        createDto = CreateUserDTO.builder()
                .username("fabio")
                .email("fabio@mail.com")
                .dni("12345678")
                .password("secret")
                .build();

        updateDto = UpdateUserDTO.builder()
                .email("new@mail.com")
                .dni("87654321")
                .active(true)
                .build();

        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("fabio");
        user.setEmail("fabio@mail.com");
        user.setDni("12345678");
        user.setPassword("encoded");
        user.setActive(true);
    }

    // ---------------- CREATE ----------------

    @Test
    void createUser_success() {
        when(userRepository.findByUsername("fabio")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("fabio@mail.com")).thenReturn(Optional.empty());
        when(userRepository.existsByDni("12345678")).thenReturn(false);
        when(mapper.toDomain(createDto)).thenReturn(user);
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(new UserResponseDTO());

        UserResponseDTO response = userService.createUser(createDto);

        assertNotNull(response);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_failsWhenEmailExists() {
        when(userRepository.findByEmail("fabio@mail.com")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(createDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_failsWhenUsernameExists() {
        when(userRepository.findByUsername("fabio")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(createDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_failsWhenDniExists() {
        when(userRepository.existsByDni("12345678")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(createDto));
        verify(userRepository, never()).save(any());
    }

    // ---------------- UPDATE ----------------

    @Test
    void updateUser_success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("new@mail.com")).thenReturn(Optional.empty());
        when(userRepository.existsByDni("87654321")).thenReturn(false);
        doAnswer(invocation -> {
            UpdateUserDTO dto = invocation.getArgument(0);
            User u = invocation.getArgument(1);
            u.setEmail(dto.getEmail());
            u.setDni(dto.getDni());
            u.setActive(dto.getActive());
            return null;
        }).when(mapper).updateDomain(updateDto, user);
        when(userRepository.save(user)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(new UserResponseDTO());

        UserResponseDTO response = userService.updateUser(user.getId(), updateDto);

        assertNotNull(response);
        assertEquals("new@mail.com", user.getEmail());
        assertEquals("87654321", user.getDni());
    }

    @Test
    void updateUser_failsWhenEmailExists() {
        User other = new User();
        other.setId(UUID.randomUUID());
        other.setEmail("new@mail.com");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("new@mail.com")).thenReturn(Optional.of(other));

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(user.getId(), updateDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_failsWhenDniExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("new@mail.com")).thenReturn(Optional.empty());
        when(userRepository.existsByDni("87654321")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(user.getId(), updateDto));
        verify(userRepository, never()).save(any());
    }
}
