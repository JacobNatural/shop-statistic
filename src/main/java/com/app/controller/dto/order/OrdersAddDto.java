package com.app.controller.dto.order;

import com.app.persistence.entity.ClientEntity;
import com.app.persistence.entity.OrderEntity;
import com.app.persistence.entity.ProductEntity;

import java.util.List;

/**
 * A DTO representing the data required to add multiple orders for a client.
 * <p>
 * This class contains the client ID and a list of product IDs that will be used to create the orders.
 * </p>
 */
public record OrdersAddDto(
        /**
         * The ID of the client placing the orders.
         */
        Long clientId,

        /**
         * A list of product IDs associated with the orders.
         */
        List<Long> productsId) {

    /**
     * Converts the provided client and product entities into a list of {@link OrderEntity} objects.
     * <p>
     * This method creates a list of orders for a client, where each order corresponds to a product from the provided list of products.
     * </p>
     *
     * @param clientEntity    the {@link ClientEntity} representing the client placing the orders.
     * @param productEntities a list of {@link ProductEntity} objects representing the products to be ordered.
     * @return a list of {@link OrderEntity} objects created from the given client and products.
     */
    public List<OrderEntity> orderEntityList(ClientEntity clientEntity, List<ProductEntity> productEntities) {
        return productEntities
                .stream()
                .map(productEntity -> new OrderEntity(clientEntity, productEntity))
                .toList();
    }
}
