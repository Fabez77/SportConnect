package com.sportconnect.authorization.permission.infrastructure.mapper;

import com.sportconnect.authorization.permission.domain.model.Permission;
import com.sportconnect.authorization.permission.infrastructure.persistence.entity.PermissionEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionEntityMapper {
    Permission toDomain(PermissionEntity entity);
    PermissionEntity toEntity(Permission domain);
    List<Permission> toDomainList(List<PermissionEntity> entities);
}
