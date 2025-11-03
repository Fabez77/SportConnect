package com.sportconnect.semana12.order.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sportconnect.semana12.order.dto.OrderRequestDTO;
import com.sportconnect.semana12.order.dto.OrderResponseDTO;
import com.sportconnect.semana12.order.entity.Order;
import com.sportconnect.semana12.order.entity.OrderItem;
import com.sportconnect.semana12.order.exception.InvalidOrderException;
import com.sportconnect.semana12.order.mapper.OrderMapper;
import com.sportconnect.semana12.order.repository.OrderRepository;
import com.sportconnect.shared.datatable.dto.DataTableRequest;
import com.sportconnect.shared.datatable.dto.DataTableResponse;
import com.sportconnect.shared.datatable.filter.SpecificationBuilder;
import com.sportconnect.shared.datatable.service.DataTableService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final OrderMapper mapper;
    private final DataTableService dataTableService;

    /**
     * Crear pedido con rollback si no tiene ítems
     */
    @Transactional(rollbackFor = InvalidOrderException.class)
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        if (dto.getItems().isEmpty()) {
            throw new InvalidOrderException("Pedido sin ítems");
        }

        Order order = new Order();
        order.setCustomer(dto.getCustomer());

        List<OrderItem> items = dto.getItems().stream()
                .map(mapper::toEntity)
                .peek(item -> item.setOrder(order))
                .toList();

        order.setItems(items);
        Order saved = orderRepo.save(order);

        return mapper.toDto(saved);
    }

    /**
     * Actualizar pedido con rollback si ocurre error
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        order.setCustomer(dto.getCustomer());

        order.getItems().clear();
        List<OrderItem> items = dto.getItems().stream()
                .map(mapper::toEntity)
                .peek(item -> item.setOrder(order))
                .toList();

        order.setItems(items);
        Order updated = orderRepo.save(order);

        return mapper.toDto(updated);
    }

    public OrderResponseDTO getOrderById(Long id) {
        return orderRepo.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    @Transactional(readOnly = true)
    public DataTableResponse<OrderResponseDTO> getOrders(DataTableRequest request) {
        Pageable pageable = dataTableService.buildPageable(request);

        SpecificationBuilder<Order> builder = new SpecificationBuilder<>();
        Specification<Order> spec = builder.build(
                request.getFilters(),
                request.getSearch(),
                List.of("customer"));

        Page<Order> page = orderRepo.findAll(spec, pageable);
        Page<OrderResponseDTO> dtoPage = page.map(mapper::toDto);

        return dataTableService.buildResponse(dtoPage);
    }

    public void deleteOrder(Long id) {
        orderRepo.deleteById(id);
    }
}
