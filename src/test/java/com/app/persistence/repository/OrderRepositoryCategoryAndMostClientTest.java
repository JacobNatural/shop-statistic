package com.app.persistence.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.app.data.OrderData.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class OrderRepositoryCategoryAndMostClientTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("When we don't have orders in the database and return an empty list.")
    public void test1() {

        Assertions.assertThat(orderRepository.getCategoryAndMostClient())
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When we have two clients with the highest number of orders in a category, return the category and the two clients.")
    public void test2() {

        orderRepository.saveAll(List.of(ORDER_ENTITY1, ORDER_ENTITY2));
        var projection = orderRepository.getCategoryAndMostClient();

        Assertions.assertThat(projection.getFirst().getId()).isEqualTo(1L);
        Assertions.assertThat(projection.get(1).getId()).isEqualTo(2L);
        Assertions.assertThat(projection.getFirst().getCategory()).isEqualTo("groceries");
        Assertions.assertThat(projection.get(1).getCategory()).isEqualTo("groceries");

    }

    @Test
    @DisplayName("When we have a few ages and one category per age, return the age with the category.")
    public void test3() {
        orderRepository
                .saveAll(List.of(ORDER_ENTITY1, ORDER_ENTITY2, ORDER_ENTITY3, ORDER_ENTITY6));
        System.out.println("--------------------------------------------");
        System.out.println(orderRepository.findAll());
        System.out.println("--------------------------------------------");
        var projection = orderRepository.getCategoryAndMostClient();
        Assertions.assertThat(projection.getFirst().getId()).isEqualTo(1);
        Assertions.assertThat(projection.get(1).getId()).isEqualTo(3);
        Assertions.assertThat(projection.getFirst().getCategory()).isEqualTo("groceries");
        Assertions.assertThat(projection.get(1).getCategory()).isEqualTo("home");
    }
}