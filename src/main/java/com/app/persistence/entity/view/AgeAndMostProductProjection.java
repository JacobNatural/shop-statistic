package com.app.persistence.entity.view;

import java.math.BigDecimal;

/**
 * A projection interface for retrieving data related to a product, along with its associated client age.
 * <p>
 * This interface is used for projection purposes, typically when querying a database to retrieve a
 * set of specific fields from one or more entities without the need to fetch the entire entity.
 * It represents a client’s age and product details (ID, name, category, and price).
 * </p>
 */
public interface AgeAndMostProductProjection {

    /**
     * Retrieves the age of the client associated with the product.
     *
     * @return The age of the client.
     */
    Integer getAge();

    /**
     * Retrieves the ID of the product.
     *
     * @return The product's ID.
     */
    Long getId();

    /**
     * Retrieves the name of the product.
     *
     * @return The product's name.
     */
    String getName();

    /**
     * Retrieves the category of the product.
     *
     * @return The product's category.
     */
    String getCategory();

    /**
     * Retrieves the price of the product.
     *
     * @return The product's price.
     */
    BigDecimal getPrice();
}
