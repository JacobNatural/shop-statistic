package com.app.converter.single.impl;

import com.app.converter.single.Converter;
import com.app.model.Order;
import com.app.persistence.entity.OrderEntity;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link Converter} interface for converting between {@link OrderEntity}
 * and {@link Order} objects.
 * <p>
 * This class provides the conversion logic for transforming an {@link OrderEntity} (representing a
 * database entity) to an {@link Order} (a business model) and vice versa.
 * </p>
 */
@Component
public class OrderConverterImpl implements Converter<OrderEntity, Order> {

    /**
     * Converts an {@link OrderEntity} to an {@link Order} model object.
     *
     * @param orderEntity the {@link OrderEntity} object to convert
     * @return the corresponding {@link Order} model
     */
    @Override
    public Order toModel(OrderEntity orderEntity) {
        return orderEntity.toOrder();
    }

    /**
     * Converts an {@link Order} model object to an {@link OrderEntity}.
     *
     * @param order the {@link Order} model to convert
     * @return the corresponding {@link OrderEntity} object
     */
    @Override
    public OrderEntity toEntity(Order order) {
        return order.toOrderEntity();
    }
}
