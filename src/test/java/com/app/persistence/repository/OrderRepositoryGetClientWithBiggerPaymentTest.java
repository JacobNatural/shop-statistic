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

import static com.app.data.ClientData.*;
import static com.app.data.OrderData.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class OrderRepositoryGetClientWithBiggerPaymentTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("When we don't have orders in the database and return an empty list.")
    public void test1() {

        Assertions.assertThat(orderRepository.getClientWithBiggerPayment())
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When we have one order in the database and return client.")
    public void test2() {

        orderRepository.save(ORDER_ENTITY1);

        Assertions.assertThat(orderRepository.getClientWithBiggerPayment())
                .contains(CLIENT_ENTITY_READ_1);
    }

    @Test
    @DisplayName("When we have many orders and one client with the largest payment in the database, we return that client.")
    public void test3() {

        orderRepository
                .saveAll(List.of(ORDER_ENTITY1, ORDER_ENTITY2, ORDER_ENTITY3));


        System.out.println(orderRepository.findAll());

        Assertions.assertThat(orderRepository.getClientWithBiggerPayment())
                .contains(CLIENT_ENTITY_READ_3);
    }


    @Test
    @DisplayName("When we have many orders and two clients with the largest payments in the database, we return those two clients.")
    public void test4() {

        orderRepository
                .saveAll(List.of(ORDER_ENTITY1, ORDER_ENTITY4, ORDER_ENTITY3));

        System.out.println(orderRepository.findAll());

        Assertions.assertThat(orderRepository.getClientWithBiggerPaymentInCategory("home"))
                .contains(CLIENT_ENTITY_READ_3, CLIENT_ENTITY_READ_2);
    }

}
