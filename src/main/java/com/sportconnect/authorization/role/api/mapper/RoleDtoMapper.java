package com.sportconnect.authorization.role.api.mapper;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.sportconnect.authorization.permission.api.mapper.PermissionDtoMapper;
import com.sportconnect.authorization.permission.domain.model.Permission;
import com.sportconnect.authorization.role.api.dto.CreateRoleDTO;
import com.sportconnect.authorization.role.api.dto.RoleResponseDTO;
import com.sportconnect.authorization.role.api.dto.UpdateRoleDTO;
import com.sportconnect.authorization.role.domain.model.Role;

@Mapper(componentModel = "spring", uses = {PermissionDtoMapper.class})
public interface RoleDtoMapper {
    Role toDomain(CreateRoleDTO dto);
    RoleResponseDTO toResponse(Role role);
    void updateDomain(UpdateRoleDTO dto, @MappingTarget Role role);

    // De Set<UUID> a Set<Permission> (stub con solo id)
    default Set<Permission> map(Set<UUID> ids) {
        if (ids == null) return Collections.emptySet();
        return ids.stream()
                .map(id -> Permission.builder().id(id).build())
                .collect(Collectors.toSet());
    }

    // De Set<Permission> a Set<String> (solo nombres)
    default Set<String> mapPermissionsToNames(Set<Permission> permissions) {
        if (permissions == null) return Collections.emptySet();
        return permissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }
}
