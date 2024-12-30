package com.app.persistence.entity.view;

/**
 * A projection interface for retrieving data related to the most frequently purchased product category
 * by a client based on their age.
 * <p>
 * This interface is used to project specific data related to the age of a client and the category of
 * products they most often purchase or interact with. It allows the system to query for only the
 * necessary data without fetching the full entity.
 * </p>
 */
public interface AgeMostOftenCategoryProjection {

    /**
     * Retrieves the age of the client.
     *
     * @return The age of the client.
     */
    Integer getAge();

    /**
     * Retrieves the category of the product most often purchased or interacted with by the client.
     *
     * @return The most frequently interacted product category.
     */
    String getCategory();
}
