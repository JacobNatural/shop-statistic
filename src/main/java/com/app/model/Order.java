package com.app.model;

import com.app.controller.dto.order.OrderFindDto;
import com.app.persistence.entity.OrderEntity;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Represents an order in the system.
 * <p>
 * This class contains details about an order, including the unique order ID, the client placing the order, and the product being ordered.
 * It provides methods to convert the order to an entity for persistence and to a DTO (Data Transfer Object) for transferring data.
 * </p>
 */
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Order {

    /**
     * The unique identifier of the order.
     * <p>
     * This ID uniquely identifies an order in the system.
     * </p>
     */
    private final Long id;

    /**
     * The client who placed the order.
     * <p>
     * This field represents the client associated with the order.
     * </p>
     */
    private final Client client;

    /**
     * The product being ordered.
     * <p>
     * This field represents the product that the client is ordering.
     * </p>
     */
    private final Product product;

    /**
     * Converts this order to an {@link OrderEntity} object.
     * <p>
     * An entity is used to represent an order in the database, allowing for persistence operations.
     * </p>
     *
     * @return a new {@link OrderEntity} object populated with this order's data
     */
    public OrderEntity toOrderEntity() {
        return OrderEntity
                .builder()
                .id(id)
                .productEntity(product.toProductEntity())
                .clientEntity(client.toClientEntity())
                .build();
    }

    /**
     * Converts this order to an {@link OrderFindDto} object.
     * <p>
     * A DTO is used for transferring data over the network or between layers of the application.
     * </p>
     *
     * @return a new {@link OrderFindDto} object populated with this order's data
     */
    public OrderFindDto toOrderFindDto() {
        return new OrderFindDto(id, client.toClientDto(), product.toProductDto());
    }
}
