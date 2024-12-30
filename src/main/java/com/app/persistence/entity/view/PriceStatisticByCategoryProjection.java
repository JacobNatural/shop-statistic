package com.app.persistence.entity.view;

import java.math.BigDecimal;

/**
 * Projection interface for retrieving price statistics by category.
 * <p>
 * This interface is used to define the structure of a result set that provides detailed price statistics
 * for each product category. It retrieves information such as average price, the minimum price with
 * associated product details, and the maximum price with associated product details.
 * </p>
 */
public interface PriceStatisticByCategoryProjection {

    /**
     * Returns the category of the product.
     * <p>
     * This is the category under which the products are classified (e.g., electronics, clothing, etc.).
     * </p>
     *
     * @return The category of the product.
     */
    String getCategory();

    /**
     * Returns the average price of products in the given category.
     * <p>
     * This represents the mean price calculated from all products within the same category.
     * </p>
     *
     * @return The average price of the products in the category.
     */
    BigDecimal getAvgPrice();

    /**
     * Returns the ID of the product with the minimum price in the given category.
     *
     * @return The ID of the product with the minimum price.
     */
    Long getMinId();

    /**
     * Returns the name of the product with the minimum price in the given category.
     *
     * @return The name of the product with the minimum price.
     */
    String getMinName();

    /**
     * Returns the minimum price in the given category.
     * <p>
     * This represents the lowest price of any product within the category.
     * </p>
     *
     * @return The minimum price in the category.
     */
    BigDecimal getMinPrice();

    /**
     * Returns the ID of the product with the maximum price in the given category.
     *
     * @return The ID of the product with the maximum price.
     */
    Long getMaxId();

    /**
     * Returns the name of the product with the maximum price in the given category.
     *
     * @return The name of the product with the maximum price.
     */
    String getMaxName();

    /**
     * Returns the maximum price in the given category.
     * <p>
     * This represents the highest price of any product within the category.
     * </p>
     *
     * @return The maximum price in the category.
     */
    BigDecimal getMaxPrice();
}
