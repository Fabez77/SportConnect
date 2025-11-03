package com.sportconnect.user.api.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {

    @NotBlank
    @Size(min = 3, max = 30)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 8)
    private String dni;

    @NotBlank
    @Size(min = 6, max = 100) // puedes ajustar el máximo si usas reglas más estrictas
    private String password;
}

