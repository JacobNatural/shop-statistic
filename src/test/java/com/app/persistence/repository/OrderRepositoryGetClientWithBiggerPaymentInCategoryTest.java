package com.app.persistence.repository;

import com.app.persistence.entity.ClientEntity;
import com.app.persistence.entity.OrderEntity;
import com.app.persistence.entity.ProductEntity;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.app.data.ClientData.*;
import static com.app.data.OrderData.*;
import static com.app.data.ProductData.*;


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
public class OrderRepositoryGetClientWithBiggerPaymentInCategoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("When we don't have orders in the database and return an empty list.")
    public void test1() {

        Assertions.assertThat(orderRepository.getClientWithBiggerPaymentInCategory("home"))
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When we have orders in the database and we search for a category that does not exist, we return an empty list.")
    public void test2() {

        OrderEntity ORDER_ENTITY1 = OrderEntity
                .builder()
                .id(1L)
                .clientEntity(CLIENT_ENTITY_READ_1)
                .productEntity(PRODUCT_ENTITY_READ1)
                .build();

        ClientEntity CLIENT_ENTITY_READ_1 = ClientEntity
                .builder()
                .id(1L)
                .name("A")
                .surname("AA")
                .age(30)
                .cash(BigDecimal.valueOf(2000))
                .build();

        ProductEntity PRODUCT_ENTITY_READ1 = ProductEntity
                .builder()
                .id(1L)
                .name("Apple")
                .category("groceries")
                .price(BigDecimal.valueOf(2.3))
                .build();
        orderRepository.save(ORDER_ENTITY1);


        Assertions.assertThat(orderRepository.getClientWithBiggerPaymentInCategory("home"))
                .isEqualTo(List.of());

    }

    @Test
    @DisplayName("When we have many orders and one client with the largest payment in a category in the database, we return that client.")
    public void test3() {

        orderRepository
                .saveAll(List.of(ORDER_ENTITY1, ORDER_ENTITY2, ORDER_ENTITY3));

        Assertions.assertThat(orderRepository.getClientWithBiggerPaymentInCategory("groceries"))
                .contains(CLIENT_ENTITY_READ_1);
    }


    @Test
    @DisplayName("When we have many orders and two clients with the largest payments in a category in the database, we return those two clients.")
    public void test4() {

        orderRepository
                .saveAll(List.of(ORDER_ENTITY1, ORDER_ENTITY4, ORDER_ENTITY3));

        System.out.println(orderRepository.findAll());

        Assertions.assertThat(orderRepository.getClientWithBiggerPayment())
                .contains(CLIENT_ENTITY_READ_3, CLIENT_ENTITY_READ_2);
    }
}