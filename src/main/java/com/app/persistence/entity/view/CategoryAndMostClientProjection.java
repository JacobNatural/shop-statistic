package com.app.persistence.entity.view;

import java.math.BigDecimal;

/**
 * A projection interface for retrieving data related to the most significant clients within
 * a specific product category.
 * <p>
 * This interface is used to project specific data about a client and the product category they
 * belong to. It allows for optimized queries that return only the essential client information
 * and their related category without fetching full entity objects.
 * </p>
 */
public interface CategoryAndMostClientProjection {

    /**
     * Retrieves the name of the product category to which the client is associated.
     *
     * @return The category of the product associated with the client.
     */
    String getCategory();

    /**
     * Retrieves the unique identifier of the client.
     *
     * @return The client's ID.
     */
    Long getId();

    /**
     * Retrieves the name of the client.
     *
     * @return The name of the client.
     */
    String getName();

    /**
     * Retrieves the surname of the client.
     *
     * @return The surname of the client.
     */
    String getSurname();

    /**
     * Retrieves the age of the client.
     *
     * @return The age of the client.
     */
    Integer getAge();

    /**
     * Retrieves the cash amount associated with the client.
     *
     * @return The cash amount the client has.
     */
    BigDecimal getCash();
}
