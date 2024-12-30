package com.app.model;

import com.app.controller.dto.product.ProductDto;
import com.app.persistence.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Represents a product in the system.
 * <p>
 * This class contains details about a product such as its unique identifier, name, category, and price.
 * It provides methods to convert the product to an entity for persistence and to a DTO (Data Transfer Object) for transferring data.
 * </p>
 */
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Product {

    /**
     * The unique identifier of the product.
     * <p>
     * This ID uniquely identifies a product in the system.
     * </p>
     */
    final long id;

    /**
     * The name of the product.
     * <p>
     * This field represents the name of the product.
     * </p>
     */
    final String name;

    /**
     * The category the product belongs to.
     * <p>
     * This field represents the category of the product (e.g., electronics, clothing).
     * </p>
     */
    final String category;

    /**
     * The price of the product.
     * <p>
     * This field represents the price of the product. It is used to calculate costs, discounts, etc.
     * </p>
     */
    final BigDecimal price;

    /**
     * Converts this product to a {@link ProductDto} object.
     * <p>
     * A DTO is used for transferring data over the network or between layers of the application.
     * </p>
     *
     * @return a new {@link ProductDto} object populated with this product's data
     */
    public ProductDto toProductDto() {
        return new ProductDto(id, name, category, price);
    }

    /**
     * Converts this product to a {@link ProductEntity} object.
     * <p>
     * An entity is used to represent the product in the database, allowing for persistence operations.
     * </p>
     *
     * @return a new {@link ProductEntity} object populated with this product's data
     */
    public ProductEntity toProductEntity() {
        return ProductEntity
                .builder()
                .id(id)
                .name(name)
                .category(category)
                .price(price)
                .build();
    }
}
