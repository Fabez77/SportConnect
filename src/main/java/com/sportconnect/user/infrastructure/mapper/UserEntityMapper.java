package com.sportconnect.user.infrastructure.mapper;

import com.sportconnect.user.api.dto.UpdateUserDTO;
import com.sportconnect.user.domain.model.User;
import com.sportconnect.user.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {com.sportconnect.authorization.role.infrastructure.mapper.RoleEntityMapper.class})
public interface UserEntityMapper {

    // User ↔ UserEntity
    User toDomain(UserEntity entity);
    UserEntity toEntity(User domain);

    // Colección
    List<User> toDomainList(List<UserEntity> entities);

    // Actualización parcial desde DTO
    void updateDomain(UpdateUserDTO dto, @MappingTarget User user);
}
