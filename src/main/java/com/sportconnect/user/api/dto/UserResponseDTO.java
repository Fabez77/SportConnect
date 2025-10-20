package com.sportconnect.user.api.dto;

import lombok.*;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private UUID id;
    private String username;
    private String email;
    private String dni;
    private boolean isActive;
    private Set<String> roles;
}
