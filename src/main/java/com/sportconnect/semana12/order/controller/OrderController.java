package com.sportconnect.semana12.order.controller;

import org.hibernate.mapping.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sportconnect.semana12.order.dto.*;
import com.sportconnect.semana12.order.exception.InvalidOrderException;
import com.sportconnect.semana12.order.service.OrderService;
import com.sportconnect.shared.apiresponse.dto.ApiResponse;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody OrderRequestDTO dto) {
        try {
            service.createOrder(dto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Pedido creado", null));
        } catch (InvalidOrderException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> listAll() {
        List<OrderResponseDTO> orders = service.getAllOrders();
        return ResponseEntity.ok(new ApiResponse<>(true, "Listado de pedidos", orders));
    }
}


