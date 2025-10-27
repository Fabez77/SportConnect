package com.sportconnect.shared.datatable.filter;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpecificationBuilder<T> {

    public Specification<T> build(Map<String, String> filters, List<String> globalSearchFields) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters != null && !filters.isEmpty()) {
                // filtros individuales
                filters.forEach((field, value) -> {
                    if (value != null && !value.isEmpty() && !"global".equalsIgnoreCase(field)) {
                        predicates.add(
                            cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%")
                        );
                    }
                });

                // búsqueda global
                if (filters.containsKey("global")) {
                    String value = filters.get("global").toLowerCase();
                    List<Predicate> orPredicates = new ArrayList<>();

                    for (String field : globalSearchFields) {
                        orPredicates.add(cb.like(cb.lower(root.get(field)), "%" + value + "%"));
                    }

                    predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
                }
            }

            // si no hay filtros → devuelve todo
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
