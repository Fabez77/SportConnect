package com.sportconnect.shared.apiresponse.service;

import com.sportconnect.shared.apiresponse.dto.ApiResponse;
import org.springframework.http.*;

public interface ApiResponseService {

    <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message, T data);

    <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message);

    <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message);
}
