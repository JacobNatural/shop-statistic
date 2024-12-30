package com.app.service.impl.ShopStatisticService;


import com.app.persistence.repository.OrderRepository;
import com.app.persistence.repository.ProductRepository;
import com.app.service.impl.ShopStatisticServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.app.data.ClientData.*;

@ExtendWith(MockitoExtension.class)
public class ShopStatisticServiceImplGetClientWithBiggerPaymentTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ShopStatisticServiceImpl shopStatisticService;

    @Test
    @DisplayName("When the repository returns an empty list, the service should also return an empty list.")
    public void test1() {

        Mockito.when(orderRepository.getClientWithBiggerPayment())
                .thenReturn(List.of());

        Assertions.assertThat(shopStatisticService.getClientWithBiggerPayment())
                .isEqualTo(List.of());

        Mockito.verify(orderRepository, Mockito.times(1))
                .getClientWithBiggerPayment();
    }

    @Test
    @DisplayName("When the repository returns one client entity, the service should return one client.")
    public void test2() {

        Mockito.when(orderRepository.getClientWithBiggerPayment())
                .thenReturn(List.of(CLIENT_ENTITY_READ_1));

        Assertions.assertThat(shopStatisticService.getClientWithBiggerPayment())
                .isEqualTo(List.of(CLIENT1));

        Mockito.verify(orderRepository, Mockito.times(1))
                .getClientWithBiggerPayment();
    }

    @Test
    @DisplayName("When the repository returns three client entities, the service should return three clients.")
    public void test3() {

        Mockito.when(orderRepository.getClientWithBiggerPayment())
                .thenReturn(List.of(CLIENT_ENTITY_READ_1, CLIENT_ENTITY_READ_2, CLIENT_ENTITY_READ_3));

        Assertions.assertThat(shopStatisticService.getClientWithBiggerPayment())
                .isEqualTo(List.of(CLIENT1, CLIENT2, CLIENT3));

        Mockito.verify(orderRepository, Mockito.times(1))
                .getClientWithBiggerPayment();
    }
}

