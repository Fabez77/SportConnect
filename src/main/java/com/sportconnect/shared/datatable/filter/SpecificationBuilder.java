package com.sportconnect.shared.datatable.filter;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpecificationBuilder<T> {

    public Specification<T> build(Map<String, String> filters, String search, List<String> globalSearchFields) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters != null && !filters.isEmpty()) {
                filters.forEach((field, value) -> {
                    if (value != null && !value.isEmpty()) {
                        predicates.add(cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%"));
                    }
                });
            }

            if (search != null && !search.isBlank()) {
                String value = search.toLowerCase();
                List<Predicate> orPredicates = new ArrayList<>();
                for (String field : globalSearchFields) {
                    orPredicates.add(cb.like(cb.lower(root.get(field)), "%" + value + "%"));
                }
                predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
