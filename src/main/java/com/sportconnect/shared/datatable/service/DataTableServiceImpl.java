package com.sportconnect.shared.datatable.service;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.sportconnect.shared.datatable.dto.DataTableRequest;
import com.sportconnect.shared.datatable.dto.DataTableResponse;
@Service
public class DataTableServiceImpl implements DataTableService {

    @Override
    public Pageable buildPageable(DataTableRequest request) {
        Sort sort = Sort.by(
            Sort.Direction.fromString(request.getDirection() != null ? request.getDirection() : "ASC"),
            request.getSortBy() != null ? request.getSortBy() : "id"
        );
        return PageRequest.of(
            request.getPage() >= 0 ? request.getPage() : 0,
            request.getSize() > 0 ? request.getSize() : 10,
            sort
        );
    }

    @Override
    public <T> DataTableResponse<T> buildResponse(Page<T> page) {
        return DataTableResponse.<T>builder()
                .data(page.getContent())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .build();
    }
}
