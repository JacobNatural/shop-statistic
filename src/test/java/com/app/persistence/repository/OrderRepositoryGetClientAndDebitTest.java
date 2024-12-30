package com.app.persistence.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static com.app.data.OrderData.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class OrderRepositoryGetClientAndDebitTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("When we don't have orders in the database and return an empty list.")
    public void test1() {

        Assertions.assertThat(orderRepository.getClientAndDebit())
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When we don't have any clients with debit, we return an empty list.")
    public void test2() {

        orderRepository.saveAll(List.of(ORDER_ENTITY1, ORDER_ENTITY2));
        Assertions.assertThat(orderRepository.getClientAndDebit())
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When we have clients with debits, and we return clients with outstanding balances.")
    public void test3() {

        orderRepository
                .saveAll(List.of(ORDER_ENTITY1, ORDER_ENTITY2, ORDER_ENTITY7, ORDER_ENTITY8));

        var projection = orderRepository.getClientAndDebit();

        Assertions.assertThat(projection.getFirst().getId())
                .isEqualTo(3L);
        Assertions.assertThat(projection.get(1).getId())
                .isEqualTo(4L);
        Assertions.assertThat(projection.getFirst().getDebit())
                .isEqualByComparingTo(BigDecimal.valueOf(-39.1));
        Assertions.assertThat(projection.get(1).getDebit())
                .isEqualByComparingTo(BigDecimal.valueOf(-19.1));
    }
}
