package com.app.service.impl.ShopStatisticService;


import com.app.persistence.repository.OrderRepository;
import com.app.persistence.repository.ProductRepository;
import com.app.service.impl.ShopStatisticServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.app.data.ClientData.*;

@ExtendWith(MockitoExtension.class)
public class ShopStatisticServiceImplGetClientWithBiggerPaymentInCategoryTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ShopStatisticServiceImpl shopStatisticService;

    @Test
    @DisplayName("When the category is null, throw an IllegalArgumentException.")
    public void test1() {

        Assertions.assertThatThrownBy(() ->
                shopStatisticService.getClientWithBiggerPaymentInCategory(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Category cannot be null");
    }

    @Test
    @DisplayName("When the category is empty, throw an IllegalArgumentException.")
    public void test2() {

        Assertions.assertThatThrownBy(() ->
                        shopStatisticService.getClientWithBiggerPaymentInCategory(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Category cannot be empty");
    }

    @Test
    @DisplayName("When the repository returns an empty list, the service should also return an empty list.")
    public void test3() {

        Mockito.when(orderRepository.getClientWithBiggerPaymentInCategory(ArgumentMatchers.anyString()))
                .thenReturn(List.of());

        Assertions.assertThat(shopStatisticService.getClientWithBiggerPaymentInCategory("home"))
                .isEqualTo(List.of());

        Mockito.verify(orderRepository, Mockito.times(1))
                .getClientWithBiggerPaymentInCategory(ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("When the repository returns one client entity, the service should return one client.")
    public void test4() {

        Mockito.when(orderRepository.getClientWithBiggerPaymentInCategory(
                ArgumentMatchers.anyString()))
                .thenReturn(List.of(CLIENT_ENTITY_READ_1));

        Assertions.assertThat(shopStatisticService.getClientWithBiggerPaymentInCategory("home"))
                .isEqualTo(List.of(CLIENT1));

        Mockito.verify(orderRepository, Mockito.times(1))
                .getClientWithBiggerPaymentInCategory(ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("When the repository returns three clients entity, the service should return three clients.")
    public void test5() {

        Mockito.when(orderRepository.getClientWithBiggerPaymentInCategory(
                        ArgumentMatchers.anyString()))
                .thenReturn(List.of(CLIENT_ENTITY_READ_1,CLIENT_ENTITY_READ_2,CLIENT_ENTITY_READ_3));

        Assertions.assertThat(shopStatisticService.getClientWithBiggerPaymentInCategory("home"))
                .isEqualTo(List.of(CLIENT1,CLIENT2,CLIENT3));

        Mockito.verify(orderRepository, Mockito.times(1))
                .getClientWithBiggerPaymentInCategory(ArgumentMatchers.anyString());
    }
}


