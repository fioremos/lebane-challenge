package com.lebane.specifications;

import com.lebane.entities.Departamento;
import com.lebane.enums.Moneda;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DepartamentoSpecifications {

    public static Specification<Departamento> conFiltros(Boolean disponible, Double min, Double max, Double metrosCuadrados, String moneda) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (disponible != null) {
                predicates.add(cb.equal(root.get("disponible"), disponible));
            }

            if (min != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("precio"), min));
            }

            if (max != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("precio"), max));
            }

            if (metrosCuadrados != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("metros_cuadrados"), metrosCuadrados));
            }

            if (moneda != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("moneda"), moneda));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}