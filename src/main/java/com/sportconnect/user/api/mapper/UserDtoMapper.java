package com.sportconnect.user.api.mapper;

import com.sportconnect.authorization.role.domain.model.Role;
import com.sportconnect.user.api.dto.*;
import com.sportconnect.user.domain.model.User;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    User toDomain(CreateUserDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDomain(UpdateUserDTO dto, @MappingTarget User user);

    UserResponseDTO toResponse(User user);

    default Set<String> mapRolesToNames(Set<Role> roles) {
        return roles == null ? Set.of() :
            roles.stream().map(Role::getName).collect(Collectors.toSet());
    }

    List<UserResponseDTO> toResponseList(List<User> users);
}
