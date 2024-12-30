package com.app.controller.dto.product;

import com.app.persistence.entity.ProductEntity;

import java.math.BigDecimal;

/**
 * A DTO representing a product with details such as name, category, and price.
 * <p>
 * This class is used to transfer product information, including the product's ID, name, category, and price.
 * </p>
 */
public record ProductDto(
        /**
         * The unique identifier of the product.
         */
        Long id,

        /**
         * The name of the product.
         */
        String name,

        /**
         * The category of the product.
         */
        String category,

        /**
         * The price of the product.
         */
        BigDecimal price) {

    /**
     * Converts this {@link ProductDto} into a {@link ProductEntity}.
     * <p>
     * This method creates a new {@link ProductEntity} instance using the name, category, and price from the DTO.
     * </p>
     *
     * @return a new {@link ProductEntity} with values from this DTO.
     */
    public ProductEntity toProductEntity() {
        return ProductEntity
                .builder()
                .name(name)
                .category(category)
                .price(price)
                .build();
    }
}
