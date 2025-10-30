package com.sportconnect.authorization.role.api.dto;

import java.util.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionsResponse {
    private Set<UUID> permissions;
}
