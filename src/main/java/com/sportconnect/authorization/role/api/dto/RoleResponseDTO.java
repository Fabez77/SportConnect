package com.sportconnect.authorization.role.api.dto;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private Set<String> permissions; // nombres de permisos
}
