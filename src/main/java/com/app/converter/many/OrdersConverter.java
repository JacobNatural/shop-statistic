package com.app.converter.many;

import com.app.controller.dto.order.OrderFindDto;
import com.app.model.Order;

import java.util.List;

/**
 * Interface for converting a list of {@link Order} entities to their corresponding {@link OrderFindDto} representations.
 * <p>
 * This interface provides a method to convert a list of {@link Order} domain model objects into a list of
 * {@link OrderFindDto} objects, which are used for API responses and data transfer.
 * </p>
 */
public interface OrdersConverter {

    /**
     * Converts a list of {@link Order} objects into a list of {@link OrderFindDto} objects.
     *
     * @param orders the list of {@link Order} objects to convert
     * @return a list of {@link OrderFindDto} objects
     */
    List<OrderFindDto> toOrderFindDto(List<Order> orders);
}
