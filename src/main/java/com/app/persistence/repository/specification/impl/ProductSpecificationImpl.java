package com.app.persistence.repository.specification.impl;

import com.app.persistence.entity.ProductEntity;
import com.app.persistence.repository.specification.ProductSpecification;
import com.app.persistence.repository.specification.specification.ProductFilterSpecification;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link ProductSpecification} interface that provides methods for filtering
 * {@link ProductEntity} objects based on specific criteria like category and price.
 */
@Component
public class ProductSpecificationImpl implements ProductSpecification {

    /**
     * Creates a dynamic filter specification for querying {@link ProductEntity} objects based on
     * the provided {@link ProductFilterSpecification}.
     *
     * @param productFilterSpecification the filter criteria to be applied to the query
     * @return a {@link Specification} representing the dynamic query filter
     */
    public Specification<ProductEntity> dynamicFilter(ProductFilterSpecification productFilterSpecification) {

        return ((root, query, cb) -> {
            Predicate p = cb.conjunction(); // Initialize the conjunction (AND condition)

            // Filter by category if the category is specified
            if (productFilterSpecification.category() != null && !productFilterSpecification.category().isEmpty()) {
                p = cb.and(
                        p,
                        cb.like(root.get("category"), productFilterSpecification.category()));
            }

            // Filter by minimum price if specified
            if (productFilterSpecification.lowPrice() != null) {
                p = cb.and(
                        p,
                        cb.greaterThanOrEqualTo(root.get("price"), productFilterSpecification.lowPrice()));
            }

            // Filter by maximum price if specified
            if (productFilterSpecification.highPrice() != null) {
                p = cb.and(
                        p,
                        cb.lessThanOrEqualTo(root.get("price"), productFilterSpecification.highPrice()));
            }

            return p; // Return the constructed predicate as the filter condition
        });
    }
}
