package com.app.controller.dto;

import com.app.persistence.entity.ClientEntity;

import java.math.BigDecimal;

/**
 * A DTO representing the data of a client.
 * <p>
 * This class contains the client's ID, name, surname, age, and cash balance.
 * It also provides a method to convert the DTO into a {@link ClientEntity} object.
 * </p>
 */
public record ClientDto(
        /**
         * The unique identifier of the client.
         */
        Long id,

        /**
         * The first name of the client.
         */
        String name,

        /**
         * The surname of the client.
         */
        String surname,

        /**
         * The age of the client.
         */
        int age,

        /**
         * The cash balance of the client.
         */
        BigDecimal cash) {

    /**
     * Converts this {@link ClientDto} into a {@link ClientEntity}.
     * <p>
     * This method creates a new {@link ClientEntity} using the provided values for name, surname, age, and cash.
     * </p>
     *
     * @return a new {@link ClientEntity} representing the client.
     */
    public ClientEntity toClientEntity() {
        return ClientEntity
                .builder()
                .name(name)
                .surname(surname)
                .age(age)
                .cash(cash)
                .build();
    }
}
