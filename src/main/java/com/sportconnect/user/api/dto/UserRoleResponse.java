package com.sportconnect.user.api.dto;

import java.util.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleResponse {
    private Set<UUID> roles;
}
