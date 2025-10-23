package com.sportconnect.auth.api.controller;

import com.sportconnect.auth.application.service.AuthService;
import com.sportconnect.auth.api.dto.LoginRequest;
import com.sportconnect.auth.api.dto.LoginResponse;
import com.sportconnect.shared.dto.ApiResponse;
import com.sportconnect.shared.service.ApiResponseService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ApiResponseService apiResponseService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return apiResponseService.success(
                HttpStatus.OK,
                "Inicio de sesión exitoso",
                response
        );
    }
}