package com.sportconnect.semana12.order.service;

import java.util.List;
import java.util.stream.Collectors;

import com.sportconnect.semana12.order.dto.OrderRequestDTO;
import com.sportconnect.semana12.order.dto.OrderResponseDTO;
import com.sportconnect.semana12.order.entity.Order;
import com.sportconnect.semana12.order.entity.OrderItem;
import com.sportconnect.semana12.order.exception.InvalidOrderException;
import com.sportconnect.semana12.order.mapper.OrderMapper;
import com.sportconnect.semana12.order.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final OrderMapper mapper;

    @Transactional(rollbackFor = InvalidOrderException.class)
    public void createOrder(OrderRequestDTO dto) throws InvalidOrderException {
        if (dto.getItems().isEmpty()) {
            throw new InvalidOrderException("Pedido sin ítems");
        }

        Order order = new Order();
        order.setCustomer(dto.getCustomer());

        List<OrderItem> items = dto.getItems().stream()
            .map(mapper::toEntity)
            .peek(item -> item.setOrder(order))
            .collect(Collectors.toList());

        order.setItems(items);
        orderRepo.save(order);
    }

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepo.findAll().stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }
}

