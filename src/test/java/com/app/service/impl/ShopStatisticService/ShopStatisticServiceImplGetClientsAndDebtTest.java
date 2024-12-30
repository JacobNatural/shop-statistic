package com.app.service.impl.ShopStatisticService;


import com.app.model.Client;
import com.app.persistence.entity.view.AgeMostOftenCategoryProjection;
import com.app.persistence.entity.view.ClientAndDebitDto;
import com.app.persistence.repository.OrderRepository;
import com.app.service.impl.ShopStatisticServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@ExtendWith(MockitoExtension.class)
public class ShopStatisticServiceImplGetClientsAndDebtTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ShopStatisticServiceImpl shopStatisticService;

    @Test
    @DisplayName("When the repository returns an empty list, the service should also return an empty map.")
    public void test1() {

        Mockito.when(orderRepository.getClientAndDebit())
                .thenReturn(List.of());

        Assertions.assertThat(shopStatisticService.getClientsAndDebit())
                .isEqualTo(Map.of());

        Mockito.verify(orderRepository, Mockito.times(1))
                .getClientAndDebit();
    }

    @Test
    @DisplayName("When the repository returns one element of ClientAndDebitDto in the list, " +
            "the service should return a map with one record.")
    public void test2() {

        var clientAndDebitDto = new ClientAndDebitDto(
                1L, null, null, 0, null, BigDecimal.valueOf(-5));

        Mockito.when(orderRepository.getClientAndDebit())
                .thenReturn(List.of(clientAndDebitDto));

        Assertions.assertThat(shopStatisticService.getClientsAndDebit())
                .isEqualTo(Map.of(
                        new Client(1L, null, null, 0, null),
                        BigDecimal.valueOf(-5)));

        Mockito.verify(orderRepository, Mockito.times(1))
                .getClientAndDebit();
    }

    @Test
    @DisplayName("When the repository returns several elements of ClientAndDebitDto in a list, " +
            "the service should return a correct map.")
    public void test3() {

        var clientAndDebitDto1 = new ClientAndDebitDto(
                1L, null, null, 0, null, BigDecimal.valueOf(-5));

        var clientAndDebitDto2 = new ClientAndDebitDto(
                2L, null, null, 0, null, BigDecimal.valueOf(-15));

        var clientAndDebitDto3 = new ClientAndDebitDto(
                3L, null, null, 0, null, BigDecimal.valueOf(-25));

        Mockito.when(orderRepository.getClientAndDebit())
                .thenReturn(List.of(clientAndDebitDto1, clientAndDebitDto2, clientAndDebitDto3));

        Assertions.assertThat(shopStatisticService.getClientsAndDebit())
                .isEqualTo(Map.of(new Client(1L, null, null, 0, null),
                        BigDecimal.valueOf(-5),
                        new Client(2L, null, null, 0, null),
                        BigDecimal.valueOf(-15),
                        new Client(3L, null, null, 0, null),
                        BigDecimal.valueOf(-25)));

        Mockito.verify(orderRepository, Mockito.times(1))
                .getClientAndDebit();
    }
}


