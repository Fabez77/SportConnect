package com.sportconnect.semana12.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sportconnect.semana12.order.dto.*;
import com.sportconnect.semana12.order.entity.*;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toEntity(OrderRequestDTO dto);

    OrderResponseDTO toDto(Order entity);

    @Mapping(target = "order", ignore = true)
    OrderItem toEntity(OrderRequestDTO.ItemDTO dto);

    OrderResponseDTO.ItemDTO toDto(OrderItem entity);
}
