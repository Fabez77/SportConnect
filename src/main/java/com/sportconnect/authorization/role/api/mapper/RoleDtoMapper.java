package com.sportconnect.authorization.role.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.sportconnect.authorization.permission.api.mapper.PermissionDtoMapper;
import com.sportconnect.authorization.role.api.dto.CreateRoleDTO;
import com.sportconnect.authorization.role.api.dto.RoleResponseDTO;
import com.sportconnect.authorization.role.api.dto.UpdateRoleDTO;
import com.sportconnect.authorization.role.domain.model.Role;

@Mapper(componentModel = "spring", uses = {PermissionDtoMapper.class})
public interface RoleDtoMapper {
    Role toDomain(CreateRoleDTO dto);
    RoleResponseDTO toResponse(Role role);
    void updateDomain(UpdateRoleDTO dto, @MappingTarget Role role);
}
