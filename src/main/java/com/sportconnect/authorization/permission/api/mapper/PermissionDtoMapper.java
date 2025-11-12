package com.sportconnect.authorization.permission.api.mapper;

import com.sportconnect.authorization.permission.api.dto.*;
import com.sportconnect.authorization.permission.domain.model.Permission;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionDtoMapper {
    Permission toDomain(CreatePermissionDTO dto);
    PermissionResponseDTO toResponse(Permission permission);
    void updateDomain(UpdatePermissionDTO dto, @MappingTarget Permission permission);
    List<PermissionResponseDTO> toResponseList(List<Permission> permissions);
}

