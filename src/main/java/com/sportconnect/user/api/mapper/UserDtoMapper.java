package com.sportconnect.user.api.mapper;

import com.sportconnect.authorization.role.domain.model.Role;
import com.sportconnect.user.api.dto.CreateUserDTO;
import com.sportconnect.user.api.dto.UpdateUserDTO;
import com.sportconnect.user.api.dto.UserResponseDTO;
import com.sportconnect.user.domain.model.User;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    // Crear un User desde CreateUserDTO
    User toDomain(CreateUserDTO dto);

    // Actualizar un User desde UpdateUserDTO (ignora nulls)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDomain(UpdateUserDTO dto, @MappingTarget User user);

    // Mapear User -> UserResponseDTO, indicando cómo mapear roles
    @Mapping(target = "roles", source = "roles")
    UserResponseDTO toResponse(User user);

    // Método auxiliar para convertir Set<Role> -> Set<String>
    default Set<String> mapRolesToNames(Set<Role> roles) {
        return roles == null ? Set.of()
                : roles.stream().map(Role::getName).collect(Collectors.toSet());
    }

    // Mapear lista de Users -> lista de UserResponseDTO
    List<UserResponseDTO> toResponseList(List<User> users);
}

