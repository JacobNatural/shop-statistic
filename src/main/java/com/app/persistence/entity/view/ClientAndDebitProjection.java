package com.app.persistence.entity.view;

import java.math.BigDecimal;

/**
 * Projection interface for retrieving client information along with their associated debit balance.
 * <p>
 * This interface is used to define the structure of a result set that contains a client's details
 * and their debit balance. Projections are typically used in Spring Data repositories to fetch
 * partial data from the database, optimizing performance by only retrieving the necessary fields.
 * </p>
 */
public interface ClientAndDebitProjection {

    /**
     * Returns the unique identifier of the client.
     *
     * @return The unique ID of the client.
     */
    Long getId();

    /**
     * Returns the name of the client.
     *
     * @return The name of the client.
     */
    String getName();

    /**
     * Returns the surname of the client.
     *
     * @return The surname of the client.
     */
    String getSurname();

    /**
     * Returns the age of the client.
     *
     * @return The age of the client.
     */
    Integer getAge();

    /**
     * Returns the current cash balance of the client.
     * <p>
     * This represents the client's available funds or financial balance.
     * </p>
     *
     * @return The cash balance of the client.
     */
    BigDecimal getCash();

    /**
     * Returns the current debit (debt) balance of the client.
     * <p>
     * This represents the amount the client owes or their outstanding financial obligations.
     * </p>
     *
     * @return The debit balance of the client.
     */
    Integer getDebit();
}
