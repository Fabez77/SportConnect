package com.sportconnect.shared.service;

import com.sportconnect.shared.dto.ApiResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
public class ApiResponseServiceIpml implements ApiResponseService {

    @Override
    public <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message, T data) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status(status.value())
                .message(message)
                .data(data)
                .build();
        return new ResponseEntity<>(response, status);
    }

    @Override
    public <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message) {
        return success(status, message, null);
    }

    @Override
    public <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status(status.value())
                .message(message)
                .data(null)
                .build();
        return new ResponseEntity<>(response, status);
    }
}
