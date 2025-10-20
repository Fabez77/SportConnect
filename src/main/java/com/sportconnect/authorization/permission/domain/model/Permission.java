package com.sportconnect.authorization.permission.domain.model;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private UUID id;
    private String name;        // ej: "CREATE_USER"
    private String description; // opcional
}
