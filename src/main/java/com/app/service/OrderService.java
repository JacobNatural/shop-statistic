package com.app.service;

import com.app.controller.dto.order.OrderAddDto;
import com.app.controller.dto.order.OrdersAddDto;
import com.app.model.Order;

import java.util.List;

/**
 * Interface for managing orders. Extends the {@link CrudService} interface to inherit basic CRUD operations
 * for {@link Order} entities. In addition to the basic CRUD operations, this service provides methods for adding single
 * or multiple orders.
 */
public interface OrderService extends CrudService<Order> {

    /**
     * Adds a new order.
     *
     * @param orderAddDto the DTO containing the order data to be added
     * @return the ID of the newly added order
     */
    Long addOrder(OrderAddDto orderAddDto);

    /**
     * Adds multiple orders.
     *
     * @param ordersAddDto the DTO containing the list of orders to be added
     * @return a list of IDs of the newly added orders
     */
    List<Long> addOrders(OrdersAddDto ordersAddDto);
}
