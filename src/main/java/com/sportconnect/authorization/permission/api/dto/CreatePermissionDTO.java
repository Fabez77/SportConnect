package com.sportconnect.authorization.permission.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePermissionDTO {

    @NotBlank(message = "El nombre del permiso es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    @NotBlank(message = "La categoría del permiso es obligatorio")
    @Size(max = 100, message = "La categoría no puede superar los 100 caracteres")
    private String category;

    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String description;
}
