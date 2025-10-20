package com.sportconnect.authorization.role.infrastructure.mapper;

import org.mapstruct.Mapper;

import com.sportconnect.authorization.permission.infrastructure.mapper.PermissionEntityMapper;
import com.sportconnect.authorization.role.domain.model.Role;
import com.sportconnect.authorization.role.infrastructure.persistence.entity.RoleEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PermissionEntityMapper.class})
public interface RoleEntityMapper {
    Role toDomain(RoleEntity entity);
    RoleEntity toEntity(Role domain);
    List<Role> toDomainList(List<RoleEntity> entities);
}
