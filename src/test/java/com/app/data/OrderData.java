package com.app.data;

import com.app.controller.dto.order.OrderAddDto;
import com.app.controller.dto.order.OrderFindDto;
import com.app.model.Order;
import com.app.persistence.entity.OrderEntity;

import static com.app.data.ClientData.*;
import static com.app.data.ProductData.*;

public interface OrderData {
    Order ORDER1 = new Order(1L, CLIENT1, PRODUCT1);
    Order ORDER2 = new Order(2L, CLIENT2, PRODUCT2);
    Order ORDER3 = new Order(3L, CLIENT3, PRODUCT3);

    OrderAddDto ORDER_ADD_DTO1 = new OrderAddDto(1L,1L);

    OrderFindDto ORDER_FIND_DTO1 = new OrderFindDto(1L, CLIENT_DTO1, PRODUCT_DTO1);
    OrderFindDto ORDER_FIND_DTO2 = new OrderFindDto(2L, CLIENT_DTO2, PRODUCT_DTO2);
    OrderFindDto ORDER_FIND_DTO3 = new OrderFindDto(3L, CLIENT_DTO3, PRODUCT_DTO3);

    OrderEntity ORDER_ENTITY1 = OrderEntity
            .builder()
            .id(1L)
            .clientEntity(CLIENT_ENTITY_READ_1)
            .productEntity(PRODUCT_ENTITY_READ1)
            .build();

    OrderEntity ORDER_ENTITY2 = OrderEntity
            .builder()
            .id(2L)
            .clientEntity(CLIENT_ENTITY_READ_2)
            .productEntity(PRODUCT_ENTITY_READ2)
            .build();

    OrderEntity ORDER_ENTITY3 = OrderEntity
            .builder()
            .id(3L)
            .clientEntity(CLIENT_ENTITY_READ_3)
            .productEntity(PRODUCT_ENTITY_READ3)
            .build();

    OrderEntity ORDER_ENTITY4 = OrderEntity
            .builder()
            .id(4L)
            .clientEntity(CLIENT_ENTITY_READ_2)
            .productEntity(PRODUCT_ENTITY_READ4)
            .build();

    OrderEntity ORDER_ENTITY5 = OrderEntity
            .builder()
            .id(5L)
            .clientEntity(CLIENT_ENTITY_READ_2)
            .productEntity(PRODUCT_ENTITY_READ3)
            .build();

    OrderEntity ORDER_ENTITY6 = OrderEntity
            .builder()
            .id(6L)
            .clientEntity(CLIENT_ENTITY_READ_1)
            .productEntity(PRODUCT_ENTITY_READ1)
            .build();

    OrderEntity ORDER_ENTITY7 = OrderEntity
            .builder()
            .id(7L)
            .clientEntity(CLIENT_ENTITY_READ_5)
            .productEntity(PRODUCT_ENTITY_READ3)
            .build();

    OrderEntity ORDER_ENTITY8 = OrderEntity
            .builder()
            .id(8L)
            .clientEntity(CLIENT_ENTITY_READ_6)
            .productEntity(PRODUCT_ENTITY_READ3)
            .build();

    OrderEntity ORDER_ENTITY9 = OrderEntity
            .builder()
            .id(9L)
            .clientEntity(CLIENT_ENTITY_READ_1)
            .productEntity(PRODUCT_ENTITY_READ1)
            .build();
}
