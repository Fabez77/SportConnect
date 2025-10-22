package com.sportconnect.auth.application.service;

import java.util.UUID;

public interface JwtService {
    String generateToken(UUID userId, String username);
    boolean validateToken(String token);
    String extractUsername(String token);
    UUID extractUserId(String token);
}
