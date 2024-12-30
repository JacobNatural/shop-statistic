package com.app.controller.dto.order;

import com.app.controller.dto.ClientDto;
import com.app.controller.dto.product.ProductDto;

/**
 * A DTO representing an order with associated client and product information.
 * <p>
 * This class contains details about a specific order, including the order ID, the associated client, and the product in the order.
 * </p>
 */
public record OrderFindDto(
        /**
         * The unique identifier of the order.
         */
        long id,

        /**
         * The {@link ClientDto} representing the client associated with the order.
         */
        ClientDto clientDto,

        /**
         * The {@link ProductDto} representing the product included in the order.
         */
        ProductDto productDto) {
}
