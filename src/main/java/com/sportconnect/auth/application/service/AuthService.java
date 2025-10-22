package com.sportconnect.auth.application.service;

import com.sportconnect.auth.api.dto.LoginRequest;
import com.sportconnect.auth.api.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    // más adelante: changePassword, refreshToken, logout, etc.
}
