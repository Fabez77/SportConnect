package com.sportconnect.auth.api.controller;

import com.sportconnect.auth.api.dto.LoginRequest;
import com.sportconnect.auth.api.dto.LoginResponse;
import com.sportconnect.auth.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
