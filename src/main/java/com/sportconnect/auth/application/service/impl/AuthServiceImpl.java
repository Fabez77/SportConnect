package com.sportconnect.auth.application.service.impl;

import com.sportconnect.auth.api.dto.LoginRequest;
import com.sportconnect.auth.api.dto.LoginResponse;
import com.sportconnect.auth.application.service.AuthService;
import com.sportconnect.auth.application.service.JwtService;
import com.sportconnect.authorization.permission.domain.model.Permission;
import com.sportconnect.authorization.role.domain.model.Role;
import com.sportconnect.user.domain.model.User;
import com.sportconnect.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        // ✅ Extraer roles del usuario
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        // ✅ Extraer permisos de los roles
        List<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .distinct()
                .toList();

        // ✅ Generar token con roles y permisos
        String token = jwtService.generateToken(user.getId(), user.getUsername(), roles, permissions);

        return new LoginResponse(token);
    }

}
