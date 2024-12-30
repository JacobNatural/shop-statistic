package com.app.controller.dto.order;

/**
 * A DTO representing the data required to add an order.
 * <p>
 * This class contains the necessary information to create a new order, including the client and product associated with the order.
 * </p>
 */
public record OrderAddDto(
        /**
         * The ID of the client placing the order.
         */
        long clientId,

        /**
         * The ID of the product being ordered.
         */
        long productId) {
}
