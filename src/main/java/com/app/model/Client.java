package com.app.model;

import com.app.controller.dto.ClientDto;
import com.app.persistence.entity.ClientEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Represents a client in the system.
 * <p>
 * This class contains information about a client, including their unique identifier, name, surname, age, and available cash.
 * It provides methods to convert the client object to a DTO (Data Transfer Object) and an entity for persistence.
 * </p>
 */
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Client {

    /**
     * The unique identifier of the client.
     * <p>
     * This ID is used to uniquely identify the client within the system.
     * </p>
     */
    final long id;

    /**
     * The first name of the client.
     */
    final String name;

    /**
     * The surname of the client.
     */
    final String surname;

    /**
     * The age of the client.
     */
    final int age;

    /**
     * The cash balance available to the client.
     * <p>
     * This value represents the client's available funds.
     * </p>
     */
    final BigDecimal cash;

    /**
     * Converts this client to a {@link ClientDto} object.
     * <p>
     * A DTO is used for transferring data over the network or between layers of the application.
     * </p>
     *
     * @return a new {@link ClientDto} object populated with this client's data
     */
    public ClientDto toClientDto() {
        return new ClientDto(id, name, surname, age, cash);
    }

    /**
     * Converts this client to a {@link ClientEntity} object.
     * <p>
     * An entity is used to represent a client in the database, allowing for persistence operations.
     * </p>
     *
     * @return a new {@link ClientEntity} object populated with this client's data
     */
    public ClientEntity toClientEntity() {
        return ClientEntity
                .builder()
                .id(id)
                .name(name)
                .surname(surname)
                .age(age)
                .cash(cash)
                .build();
    }
}
