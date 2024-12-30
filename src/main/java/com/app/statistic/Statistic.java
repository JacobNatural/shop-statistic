package com.app.statistic;

import com.app.controller.dto.product.StatisticDto;
import com.app.converter.many.ProductsConverter;
import com.app.model.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface representing a statistic for a collection of products with a specific type of value (e.g., price).
 * The statistics include the minimum and maximum values (e.g., product prices), as well as average values.
 * This interface provides methods for merging statistics, converting them to DTOs, and retrieving the minimum and maximum values.
 *
 * @param <T> the type of product being used in the statistic (usually {@link Product})
 * @param <U> the type of the value associated with the statistic (e.g., {@link BigDecimal} for prices)
 */
public interface Statistic<T, U> {

    /**
     * Merges the current statistic with another one, combining their minimum and maximum values.
     * This method is used to aggregate statistics from different data sources or categories.
     *
     * @param statistic the {@link Statistic} object to merge with
     * @return the merged {@link Statistic} instance
     */
    Statistic<T, U> merge(Statistic<Product, BigDecimal> statistic);

    /**
     * Converts the statistical data into a {@link StatisticDto} object for easier consumption on the client side.
     * This DTO will contain the minimum and maximum values as well as the average.
     *
     * @param productsConverterImpl the converter used to transform product entities into DTOs
     * @return a {@link StatisticDto} object containing the minimum, maximum, and average values
     */
    StatisticDto toStatisticDto(ProductsConverter productsConverterImpl);

    /**
     * Retrieves the list of products with the minimum value (e.g., the lowest priced products).
     *
     * @return a list of {@link Product} objects that have the minimum value for the statistic
     */
    List<Product> getMin();

    /**
     * Retrieves the list of products with the maximum value (e.g., the highest priced products).
     *
     * @return a list of {@link Product} objects that have the maximum value for the statistic
     */
    List<Product> getMax();
}
