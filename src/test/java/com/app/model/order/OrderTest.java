package com.app.model.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.app.data.OrderData.*;

public class OrderTest {

    @Test
    @DisplayName("When converting a order to a Order entity.")
    public void test1(){
        Assertions.assertThat(ORDER1.toOrderEntity())
                .isEqualTo(ORDER_ENTITY1);
    }

    @Test
    @DisplayName("When converting a order to a OrderFindDto.")
    public void test2(){
        Assertions.assertThat(ORDER1.toOrderFindDto())
                .isEqualTo(ORDER_FIND_DTO1);
    }
}
