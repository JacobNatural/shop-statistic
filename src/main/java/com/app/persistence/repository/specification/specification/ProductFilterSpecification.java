package com.app.persistence.repository.specification.specification;

import java.math.BigDecimal;

/**
 * A record that represents the filter criteria for querying {@link com.app.persistence.entity.ProductEntity}
 * objects based on price range and category.
 *
 * <p>This record encapsulates the lower and upper price bounds as well as the category for filtering
 * products in a query.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 * ProductFilterSpecification filter = new ProductFilterSpecification(BigDecimal.valueOf(100), BigDecimal.valueOf(500), "Electronics");
 * </pre>
 *
 * @param lowPrice the minimum price of the product, or {@code null} if no lower bound is specified
 * @param highPrice the maximum price of the product, or {@code null} if no upper bound is specified
 * @param category the category of the product, or {@code null} if no category is specified
 */
public record ProductFilterSpecification(BigDecimal lowPrice, BigDecimal highPrice, String category) { }
