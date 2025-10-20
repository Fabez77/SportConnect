package com.sportconnect.user.infrastructure.mapper;

import com.sportconnect.authorization.permission.domain.model.Permission;
import com.sportconnect.authorization.permission.infrastructure.persistence.entity.PermissionEntity;
import com.sportconnect.authorization.role.domain.model.Role;
import com.sportconnect.authorization.role.infrastructure.persistence.entity.RoleEntity;
import com.sportconnect.user.api.dto.UpdateUserDTO;
import com.sportconnect.user.domain.model.*;
import com.sportconnect.user.infrastructure.persistence.entity.*;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    // User ↔ UserEntity
    User toDomain(UserEntity entity);
    UserEntity toEntity(User domain);

    // Role ↔ RoleEntity
    Role toDomain(RoleEntity entity);
    RoleEntity toEntity(Role domain);

    // Permission ↔ PermissionEntity
    Permission toDomain(PermissionEntity entity);
    PermissionEntity toEntity(Permission domain);

    // Colecciones con nombres únicos
    List<User> toDomainList(List<UserEntity> entities);

    Set<Role> mapRolesToDomain(Set<RoleEntity> entities);
    Set<RoleEntity> mapRolesToEntity(Set<Role> domain);

    Set<Permission> mapPermissionsToDomain(Set<PermissionEntity> entities);
    Set<PermissionEntity> mapPermissionsToEntity(Set<Permission> domain);

    // 👇 Método para actualizar un User existente con datos del DTO
    void updateDomain(UpdateUserDTO dto, @MappingTarget User user);

    // 👇 Si devuelves DTOs en responses
    // UserResponseDTO toResponse(User user);
}

