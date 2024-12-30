package com.app.converter.many.impl;

import com.app.controller.dto.order.OrderFindDto;
import com.app.converter.many.OrdersConverter;
import com.app.model.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of the {@link OrdersConverter} interface for converting
 * {@link Order} entities to {@link OrderFindDto} data transfer objects (DTOs).
 *
 * <p>This component is responsible for converting a list of {@link Order} objects
 * into a list of {@link OrderFindDto} objects for use in API responses.</p>
 */
@Component
public class OrdersConverterImpl implements OrdersConverter {

    /**
     * Converts a list of {@link Order} entities to a list of {@link OrderFindDto} objects.
     *
     * @param orders the list of {@link Order} entities to convert
     * @return a list of {@link OrderFindDto} objects representing the orders
     */
    @Override
    public List<OrderFindDto> toOrderFindDto(List<Order> orders) {
        return orders
                .stream()
                .map(Order::toOrderFindDto)
                .toList();
    }
}
