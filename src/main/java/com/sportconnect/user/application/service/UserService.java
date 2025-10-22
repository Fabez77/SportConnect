package com.sportconnect.user.application.service;

import com.sportconnect.user.api.dto.*;
import com.sportconnect.user.api.mapper.UserDtoMapper;
import com.sportconnect.user.domain.model.User;
import com.sportconnect.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserDtoMapper mapper;

    @Override
    public UserResponseDTO createUser(CreateUserDTO dto) {

        // Validar unicidad de username
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        // Validar unicidad de email
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Validar unicidad de dni
        if (userRepository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }

        User user = mapper.toDomain(dto);

        // Hashear la contraseña antes de guardar
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(hashedPassword);

        user.setId(UUID.randomUUID());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setActive(true);

        User saved = userRepository.save(user);
        return mapper.toResponse(saved);
    }

    @Override
    public UserResponseDTO updateUser(UUID id, UpdateUserDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Validar email duplicado (si cambió)
        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(user.getEmail())) {
            userRepository.findByEmail(dto.getEmail()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new IllegalArgumentException("El email ya está en uso por otro usuario");
                }
            });
        }

        // Validar dni duplicado (si cambió)
        if (dto.getDni() != null && !dto.getDni().equalsIgnoreCase(user.getDni())) {
            if (userRepository.existsByDni(dto.getDni())) {
                throw new IllegalArgumentException("El DNI ya está en uso por otro usuario");
            }
        }
        mapper.updateDomain(dto, user);
        user.setUpdatedAt(LocalDateTime.now());
        User updated = userRepository.save(user);
        return mapper.toResponse(updated);
    }

    @Override
    public UserResponseDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return mapper.toResponse(user);
    }

    @Override
    public List<UserResponseDTO> listUsers() {
        List<User> users = userRepository.findAll();
        return mapper.toResponseList(users);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.delete(id);
    }
}
