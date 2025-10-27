package com.sportconnect.shared.datatable.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTableResponse<T> {
    private List<T> data;
    private long totalElements;
    private int totalPages;
    private int currentPage;
}
