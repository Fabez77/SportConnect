package com.sportconnect.auth.api.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String username; // o email, según prefieras
    private String password;
}
