package com.sportconnect.user.application.service.impl;

import com.sportconnect.authorization.role.domain.model.Role;
import com.sportconnect.authorization.role.domain.repository.RoleRepository;
import com.sportconnect.user.api.dto.*;
import com.sportconnect.user.api.mapper.UserDtoMapper;
import com.sportconnect.user.application.service.UserService;
import com.sportconnect.user.domain.model.User;
import com.sportconnect.user.domain.repository.UserRepository;
import com.sportconnect.shared.datatable.dto.DataTableRequest;
import com.sportconnect.shared.datatable.dto.DataTableResponse;
import com.sportconnect.shared.datatable.service.DataTableService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDtoMapper mapper;
    private final DataTableService dataTableService;

    @Override
    public UserResponseDTO createUser(CreateUserDTO dto) {
        // Validaciones de unicidad
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        if (userRepository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }

        User user = mapper.toDomain(dto);

        // Hashear contraseña
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setId(UUID.randomUUID());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setActive(true);

        return mapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponseDTO updateUser(UUID id, UpdateUserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Validar email duplicado
        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(user.getEmail())) {
            userRepository.findByEmail(dto.getEmail()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new IllegalArgumentException("El email ya está en uso por otro usuario");
                }
            });
        }

        // Validar dni duplicado
        if (dto.getDni() != null && !dto.getDni().equalsIgnoreCase(user.getDni())) {
            if (userRepository.existsByDni(dto.getDni())) {
                throw new IllegalArgumentException("El DNI ya está en uso por otro usuario");
            }
        }

        mapper.updateDomain(dto, user);
        user.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponseDTO getUserById(UUID id) {
        return userRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public DataTableResponse<UserResponseDTO> getUsers(DataTableRequest request) {
        Pageable pageable = dataTableService.buildPageable(request);

        // El servicio NO construye Specification, solo pasa filtros y search
        Page<User> page = userRepository.findAll(
                request.getFilters(),
                request.getSearch(),
                pageable);

        Page<UserResponseDTO> dtoPage = page.map(mapper::toResponse);
        return dataTableService.buildResponse(dtoPage);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public void assignRoles(UUID userId, List<UUID> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        List<Role> roles = roleRepository.findAllById(roleIds);

        if (roles.size() != roleIds.size()) {
            throw new IllegalArgumentException("Algunos roles no existen");
        }

        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
    }

    @Override
    public UserRoleResponse getUserRolesIds(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Set<UUID> roleIds = user.getRoles()
                .stream()
                .map(Role::getId)
                .collect(Collectors.toSet());

        return new UserRoleResponse(roleIds);
    }
}
