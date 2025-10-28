package com.sportconnect.shared.datatable.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sportconnect.shared.datatable.dto.DataTableRequest;
import com.sportconnect.shared.datatable.dto.DataTableResponse;

public interface DataTableService {
    Pageable buildPageable(DataTableRequest request);
    <T> DataTableResponse<T> buildResponse(Page<T> page);
}
