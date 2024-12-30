package com.app.persistence.repository.specification;

import com.app.persistence.entity.ProductEntity;
import com.app.persistence.repository.specification.specification.ProductFilterSpecification;
import org.springframework.data.jpa.domain.Specification;

/**
 * Interface defining the contract for filtering {@link ProductEntity} objects.
 *
 * <p>Implementations of this interface should provide logic to apply dynamic filters
 * to product queries, allowing filtering based on different criteria such as price
 * range and category.</p>
 *
 * <p>For example, a filter might specify a range of prices or a specific product category
 * to narrow down search results.</p>
 *
 * <p>Usage example:</p>
 * {@code
 * ProductFilterSpecification filter = new ProductFilterSpecification(
 *     BigDecimal.valueOf(100),
 *     BigDecimal.valueOf(500),
 *     "Electronics"
 * );
 *
 * Specification<com.app.persistence.entity.ProductEntity> specification =
 *     productSpecification.dynamicFilter(filter);
 * }
 *
 * @see com.app.persistence.entity.ProductEntity
 * @see ProductFilterSpecification
 */
public interface ProductSpecification {

    /**
     * Generates a {@link Specification} based on the provided filter criteria.
     *
     * @param productFilterSpecification the filter criteria (price range and category)
     * @return a {@link Specification} that can be used in a query to filter {@link ProductEntity} objects
     */
    Specification<ProductEntity> dynamicFilter(ProductFilterSpecification productFilterSpecification);
}
