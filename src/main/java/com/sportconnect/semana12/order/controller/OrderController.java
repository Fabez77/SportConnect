package com.sportconnect.semana12.order.controller;

import com.sportconnect.semana12.order.dto.OrderRequestDTO;
import com.sportconnect.semana12.order.dto.OrderResponseDTO;
import com.sportconnect.semana12.order.service.OrderService;
import com.sportconnect.shared.apiresponse.dto.ApiResponse;
import com.sportconnect.shared.apiresponse.service.ApiResponseService;
import com.sportconnect.shared.datatable.dto.DataTableRequest;
import com.sportconnect.shared.datatable.dto.DataTableResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ApiResponseService responseService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDTO>> create(@Valid @RequestBody OrderRequestDTO dto) {
        OrderResponseDTO order = orderService.createOrder(dto); // 👈 transaccional en el service
        return responseService.success(HttpStatus.CREATED, "Pedido creado", order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequestDTO dto) {
        OrderResponseDTO updated = orderService.updateOrder(id, dto); // 👈 transaccional en el service
        return responseService.success(HttpStatus.OK, "Pedido actualizado", updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getById(@PathVariable Long id) {
        OrderResponseDTO order = orderService.getOrderById(id);
        return responseService.success(HttpStatus.OK, "Pedido encontrado", order);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DataTableResponse<OrderResponseDTO>>> getOrders(DataTableRequest request) {
        DataTableResponse<OrderResponseDTO> orders = orderService.getOrders(request); // 👈 listado paginado
        return responseService.success(HttpStatus.OK, "Lista de pedidos paginada", orders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
        // Opción 1: semánticamente correcto (204 sin body)
        return responseService.success(HttpStatus.NO_CONTENT, null);
        // Opción 2: consistente con permisos (200 con mensaje)
        // return responseService.success(HttpStatus.OK, "Pedido eliminado");
    }
}
