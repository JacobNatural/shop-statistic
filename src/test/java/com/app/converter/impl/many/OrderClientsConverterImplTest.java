package com.app.converter.impl.many;

import com.app.converter.many.OrdersConverter;
import com.app.converter.many.impl.OrdersConverterImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.app.data.OrderData.*;

@SpringBootTest(classes = OrdersConverterImpl.class)
public class OrderClientsConverterImplTest {

    @Autowired
    private OrdersConverter ordersConverter;

    @Test
    @DisplayName("When converting an empty list of orders to an empty list of OrderFindDto.")
    public void test1() {
        Assertions.assertThat(ordersConverter.toOrderFindDto(List.of()))
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When converting a list of orders to a list of OrderFindDto.")
    public void test2() {
        Assertions.assertThat(ordersConverter
                        .toOrderFindDto(List.of(ORDER1, ORDER2, ORDER3)))
                .contains(ORDER_FIND_DTO1, ORDER_FIND_DTO2, ORDER_FIND_DTO3);
    }
}
