package com.sportconnect.auth.infrastructure.mapper;

import com.sportconnect.auth.domain.model.UserAuth;
import com.sportconnect.auth.infrastructure.persistence.UserAuthEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAuthMapper {
    UserAuth toDomain(UserAuthEntity entity);
    UserAuthEntity toEntity(UserAuth domain);
}