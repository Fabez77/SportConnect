package com.sportconnect.authorization.role.domain.model;

import java.util.Set;
import java.util.UUID;

import com.sportconnect.authorization.permission.domain.model.Permission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private UUID id;
    private String name;
    private String description;
    private Set<Permission> permissions; // si ya tienes Permission como entidad
}

