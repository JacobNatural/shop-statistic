package com.app.persistence.entity.view;

import lombok.*;

import java.math.BigDecimal;

/**
 * A Data Transfer Object (DTO) representing a client along with their associated debit value.
 * <p>
 * This class is used to transfer client data along with their debit balance. The DTO is commonly
 * used in scenarios where the client data and their financial situation (debt) need to be handled
 * together for reporting, transactions, or other business logic.
 * </p>
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ClientAndDebitDto {

    /**
     * The unique identifier of the client.
     */
    private Long id;

    /**
     * The name of the client.
     */
    private String name;

    /**
     * The surname of the client.
     */
    private String surname;

    /**
     * The age of the client.
     */
    private Integer age;

    /**
     * The current cash balance of the client.
     * <p>
     * This could represent the client's available funds or financial balance.
     * </p>
     */
    private BigDecimal cash;

    /**
     * The current debit (debt) balance of the client.
     * <p>
     * This could represent the amount the client owes or their outstanding financial obligations.
     * </p>
     */
    private BigDecimal debit;
}
