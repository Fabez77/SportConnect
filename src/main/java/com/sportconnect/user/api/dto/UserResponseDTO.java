package com.sportconnect.user.api.dto;

import lombok.*;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private UUID id;
    private String username;
    private String email;
    private String dni;
    private boolean active;
    private Set<String> roles;
}
