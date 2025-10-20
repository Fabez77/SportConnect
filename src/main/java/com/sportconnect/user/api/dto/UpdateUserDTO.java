package com.sportconnect.user.api.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;
}

