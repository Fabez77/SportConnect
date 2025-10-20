package com.sportconnect.user.domain.model;


import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;
    private String username;
    private String email;
    private String dni;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Set<Role> roles;

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void assignRole(Role role) {
        if (roles == null) roles = new HashSet<>();
        roles.add(role);
    }

    public void removeRole(Role role) {
        if (roles != null) roles.remove(role);
    }

    public boolean hasPermission(String permissionName) {
        return roles != null && roles.stream()
            .flatMap(role -> role.getPermissions().stream())
            .anyMatch(p -> p.getName().equalsIgnoreCase(permissionName));
    }
}
