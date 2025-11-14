package com.sportconnect.auth.application.service;

import java.util.List;
import java.util.UUID;

public interface JwtService {
   String generateToken(UUID userId, String username, List<String> roles, List<String> permissions);
    boolean validateToken(String token);
    String extractUsername(String token);
    UUID extractUserId(String token);
}
