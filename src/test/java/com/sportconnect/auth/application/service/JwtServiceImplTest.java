package com.sportconnect.auth.application.service;

import com.sportconnect.auth.application.service.impl.JwtServiceImpl;
import com.sportconnect.auth.config.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        JwtProperties props = new JwtProperties();
        props.setSecret("claveSuperSecretaSoloParaDev1234567890");
        props.setExpiration(3600000); // 1 hora
        jwtService = new JwtServiceImpl(props);
    }

    @Test
    void generateAndValidateToken_success() {
        UUID userId = UUID.randomUUID();
        String token = jwtService.generateToken(userId, "fabio");

        assertNotNull(token);
        assertTrue(jwtService.validateToken(token));
        assertEquals("fabio", jwtService.extractUsername(token));
        assertEquals(userId, jwtService.extractUserId(token));
    }

    @Test
    void validateToken_invalidToken_returnsFalse() {
        assertFalse(jwtService.validateToken("invalid.token.value"));
    }
}
