package com.sportconnect.shared.datatable.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTableRequest {
    private int page;              // página actual (0-based)
    private int size;              // tamaño de página
    private String sortBy;         // campo a ordenar
    private String direction;      // ASC o DESC
    private String search;
    private Map<String, String> filters; // filtros dinámicos opcionales
}
