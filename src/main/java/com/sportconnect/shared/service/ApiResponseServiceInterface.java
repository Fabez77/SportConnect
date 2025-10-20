package com.sportconnect.shared.service;

import com.sportconnect.shared.dto.ApiResponse;
import org.springframework.http.*;

public interface ApiResponseServiceInterface {

    <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message, T data);

    <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message);

    <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message);
}
