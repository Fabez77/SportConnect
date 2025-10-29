package com.sportconnect.authorization.role.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleDTO {

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;

    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String description;

    // Opcional: asignar permisos al crear
    // private Set<UUID> permissions;
}
