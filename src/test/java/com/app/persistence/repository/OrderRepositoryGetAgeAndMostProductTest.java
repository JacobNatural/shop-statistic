package com.app.persistence.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.app.data.OrderData.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class OrderRepositoryGetAgeAndMostProductTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("When we don't have orders in the database and return an empty list.")
    public void test1() {

        Assertions.assertThat(orderRepository.getAgeAndMostProduct())
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When we have one age and the two most ordered products, return the age and the two products..")
    public void test2() {

        orderRepository.saveAll(List.of(ORDER_ENTITY1, ORDER_ENTITY5));
        var projection = orderRepository.getAgeAndMostProduct();

        Assertions.assertThat(projection.getFirst().getAge()).isEqualTo(30);
        Assertions.assertThat(projection.get(1).getAge()).isEqualTo(30);
        Assertions.assertThat(projection.getFirst().getId()).isEqualTo(1L);
        Assertions.assertThat(projection.get(1).getId()).isEqualTo(2L);

    }

    @Test
    @DisplayName("When we have a few ages and one category per age, return the age with the category.")
    public void test3() {

        orderRepository
                .saveAll(List.of(ORDER_ENTITY1, ORDER_ENTITY2, ORDER_ENTITY3, ORDER_ENTITY6));

        var projection = orderRepository.getAgeAndMostProduct();

        Assertions.assertThat(projection.getFirst().getAge()).isEqualTo(30);
        Assertions.assertThat(projection.get(1).getAge()).isEqualTo(11);
        Assertions.assertThat(projection.getFirst().getId()).isEqualTo(1L);
        Assertions.assertThat(projection.get(1).getId()).isEqualTo(3L);
    }
}