package com.sportconnect.auth.api.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserCommandDTO {
    private String username;
    private String email;
    private String password;
}
