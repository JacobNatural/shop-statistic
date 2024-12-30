package com.app.controller.dto.product;

import java.math.BigDecimal;
import java.util.List;

/**
 * A DTO representing statistical information about a collection of products.
 * <p>
 * This class contains the minimum, maximum, and average values of a set of products' attributes (e.g., price).
 * </p>
 */
public record StatisticDto(
        /**
         * A list of {@link ProductDto} representing the products with the minimum value of the attribute being measured.
         */
        List<ProductDto> min,

        /**
         * A list of {@link ProductDto} representing the products with the maximum value of the attribute being measured.
         */
        List<ProductDto> max,

        /**
         * The average value of the attribute being measured, represented as a {@link BigDecimal}.
         */
        BigDecimal avg) {
}
