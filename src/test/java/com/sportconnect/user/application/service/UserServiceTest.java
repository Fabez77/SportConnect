package com.sportconnect.user.application.service;

import com.sportconnect.user.api.dto.*;
import com.sportconnect.user.api.mapper.UserDtoMapper;
import com.sportconnect.user.domain.model.User;
import com.sportconnect.user.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        user = User.builder()
                .id(userId)
                .username("fabio")
                .email("fabio@mail.com")
                .dni("12345678")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        responseDTO = new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getDni(),
                user.isActive(),
                Set.of()
        );
    }

    @Test
    void createUser_shouldReturnUserResponseDTO() {
        CreateUserDTO dto = new CreateUserDTO("fabio", "fabio@mail.com", "12345678");

        when(mapper.toDomain(dto)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals(dto.getUsername(), result.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_shouldReturnUpdatedUserResponseDTO() {
        UpdateUserDTO dto = new UpdateUserDTO("fabio_updated", "fabio_updated@mail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doAnswer(invocation -> {
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());
            return null;
        }).when(mapper).updateDomain(eq(dto), any(User.class));
        when(userRepository.save(user)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.updateUser(userId, dto);

        assertNotNull(result);
        assertEquals(dto.getUsername(), result.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    void getUserById_shouldReturnUserResponseDTO() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.toResponse(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository).findById(userId);
    }

    @Test
    void listUsers_shouldReturnListOfUserResponseDTO() {
        List<User> users = List.of(user);
        List<UserResponseDTO> responseList = List.of(responseDTO);

        when(userRepository.findAll()).thenReturn(users);
        when(mapper.toResponseList(users)).thenReturn(responseList);

        List<UserResponseDTO> result = userService.listUsers();

        assertEquals(1, result.size());
        assertEquals(user.getUsername(), result.get(0).getUsername());
        verify(userRepository).findAll();
    }

    @Test
    void deleteUser_shouldCallRepositoryDelete() {
        doNothing().when(userRepository).delete(userId);

        userService.deleteUser(userId);

        verify(userRepository).delete(userId);
    }
}
