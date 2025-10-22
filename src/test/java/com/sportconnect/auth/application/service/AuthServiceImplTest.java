package com.sportconnect.auth.application.service;

import com.sportconnect.auth.api.dto.LoginRequest;
import com.sportconnect.auth.api.dto.LoginResponse;
import com.sportconnect.auth.application.service.impl.AuthServiceImpl;
import com.sportconnect.user.domain.model.User;
import com.sportconnect.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;

    @InjectMocks private AuthServiceImpl authService;

    @Test
    void login_success() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .username("fabio")
                .password("hashed")
                .build();

        when(userRepository.findByUsername("fabio")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret", "hashed")).thenReturn(true);
        when(jwtService.generateToken(userId, "fabio")).thenReturn("jwt-token");

        LoginResponse response = authService.login(new LoginRequest("fabio", "secret"));

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void login_userNotFound_throwsException() {
        when(userRepository.findByUsername("fabio")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> authService.login(new LoginRequest("fabio", "secret")));
    }

    @Test
    void login_invalidPassword_throwsException() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("fabio")
                .password("hashed")
                .build();

        when(userRepository.findByUsername("fabio")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> authService.login(new LoginRequest("fabio", "wrong")));
    }
}
