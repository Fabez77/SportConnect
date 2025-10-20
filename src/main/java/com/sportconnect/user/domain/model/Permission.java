package com.sportconnect.user.domain.model;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private UUID id;
    private String name;
}
