package com.sportconnect.auth.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuth {
    private UUID id;
    private String username;
    private String email;
    private String passwordHash;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}