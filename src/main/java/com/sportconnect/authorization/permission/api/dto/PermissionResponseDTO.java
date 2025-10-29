package com.sportconnect.authorization.permission.api.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private String category;
}
