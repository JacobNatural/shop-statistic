package com.app.controller.dto.product;

import com.app.persistence.repository.specification.specification.ProductFilterSpecification;

import java.math.BigDecimal;

/**
 * A DTO representing the filters that can be applied when retrieving products.
 * <p>
 * This class contains fields for filtering products by price range (low and high prices) and category.
 * </p>
 */
public record ProductsFilterDto(
        /**
         * The minimum price for the product filter.
         */
        BigDecimal lowPrice,

        /**
         * The maximum price for the product filter.
         */
        BigDecimal highPrice,

        /**
         * The category of the products to filter.
         */
        String category) {

    /**
     * Converts this {@link ProductsFilterDto} into a {@link ProductFilterSpecification}.
     * <p>
     * This method creates a {@link ProductFilterSpecification} object that can be used to filter products based on the given
     * price range and category.
     * </p>
     *
     * @return a {@link ProductFilterSpecification} object based on the values in this DTO.
     */
    public ProductFilterSpecification toProductFilterSpecification() {
        return new ProductFilterSpecification(lowPrice, highPrice, category);
    }
}
