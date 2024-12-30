package com.app.converter.impl.single;

import com.app.converter.single.Converter;
import com.app.converter.single.impl.OrderConverterImpl;
import com.app.model.Order;
import com.app.persistence.entity.OrderEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.app.data.OrderData.ORDER1;
import static com.app.data.OrderData.ORDER_ENTITY1;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OrderConverterImpl.class)
public class OrderConverterImplTest {

    @Autowired
    private Converter<OrderEntity, Order> converter;

    @Test
    @DisplayName("When convert product entity to product.")
    public void test1(){
        Assertions.assertThat(converter.toModel(ORDER_ENTITY1))
                .isEqualTo(ORDER1);
    }

    @Test
    @DisplayName("When convert client to entity client.")
    public void test2(){
        Assertions.assertThat(converter.toEntity(ORDER1))
                .isEqualTo(ORDER_ENTITY1);
    }
}

