package com.app.service.impl.ShopStatisticService;


import com.app.model.Client;
import com.app.persistence.entity.view.AgeMostOftenCategoryProjection;
import com.app.persistence.entity.view.CategoryAndMostClientProjection;
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

import java.util.List;
import java.util.Map;


@ExtendWith(MockitoExtension.class)
public class ShopStatisticServiceImplGetCategoryAndMostClientTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ShopStatisticServiceImpl shopStatisticService;

    @Test
    @DisplayName("When the repository returns an empty list, the service should also return an empty map.")
    public void test1() {

        Mockito.when(orderRepository.getCategoryAndMostClient())
                .thenReturn(List.of());

        Assertions.assertThat(shopStatisticService.getCategoryAndMostClient())
                .isEqualTo(Map.of());

        Mockito.verify(orderRepository, Mockito.times(1))
                .getCategoryAndMostClient();
    }

    @Test
    @DisplayName("When the repository returns one element of CategoryAndMostClientProjection in the list, " +
            "the service should return a map with one record.")
    public void test2() {

        var projection = Mockito.mock(CategoryAndMostClientProjection.class);

        Mockito.when(projection.getCategory())
                .thenReturn("home");
        Mockito.when(projection.getId())
                .thenReturn(1L);

        Mockito.when(orderRepository.getCategoryAndMostClient())
                .thenReturn(List.of(projection));

        Assertions.assertThat(shopStatisticService.getCategoryAndMostClient())
                .isEqualTo(Map.of("home", List.of(
                        new Client(1L, null, null, 0, null))));

        Mockito.verify(orderRepository, Mockito.times(1))
                .getCategoryAndMostClient();
    }

    @Test
    @DisplayName("When the repository returns a list with two clients in one category group," +
            " the service should return a merged map.")
    public void test3() {

        var projection1 = Mockito.mock(CategoryAndMostClientProjection.class);
        var projection2 = Mockito.mock(CategoryAndMostClientProjection.class);

        Mockito.when(projection1.getCategory())
                .thenReturn("home");

        Mockito.when(projection1.getId())
                .thenReturn(1L);

        Mockito.when(projection2.getId())
                .thenReturn(2L);

        Mockito.when(projection2.getCategory())
                .thenReturn("home");

        Mockito.when(orderRepository.getCategoryAndMostClient())
                .thenReturn(List.of(projection1, projection2));

        Assertions.assertThat(shopStatisticService.getCategoryAndMostClient())
                .isEqualTo(Map.of("home", List.of(
                        new Client(1L, null, null, 0, null),
                        new Client(2L, null, null, 0, null))));

        Mockito.verify(orderRepository, Mockito.times(1))
                .getCategoryAndMostClient();
    }

    @Test
    @DisplayName("When the repository returns a list with several categories and clients, it should return a correct map.")
    public void test4() {

        var projection1 = Mockito.mock(CategoryAndMostClientProjection.class);
        var projection2 = Mockito.mock(CategoryAndMostClientProjection.class);
        var projection3 = Mockito.mock(CategoryAndMostClientProjection.class);
        var projection4 = Mockito.mock(CategoryAndMostClientProjection.class);

        Mockito.when(projection1.getId())
                .thenReturn(1L);
        Mockito.when(projection1.getCategory())
                .thenReturn("home");

        Mockito.when(projection2.getId())
                .thenReturn(2L);
        Mockito.when(projection2.getCategory())
                .thenReturn("home");

        Mockito.when(projection3.getId())
                .thenReturn(3L);
        Mockito.when(projection3.getCategory())
                .thenReturn("automotive");

        Mockito.when(projection4.getId())
                .thenReturn(4L);
        Mockito.when(projection4.getCategory())
                .thenReturn("garden");

        Mockito.when(orderRepository.getCategoryAndMostClient())
                .thenReturn(List.of(projection1, projection2, projection3, projection4));

        Assertions.assertThat(shopStatisticService.getCategoryAndMostClient())
                .isEqualTo(Map.of("home", List.of(
                                new Client(1L, null, null, 0, null),
                                new Client(2L, null, null, 0, null)),
                        "automotive", List.of(
                                new Client(3L, null, null, 0, null)),
                        "garden", List.of(
                                new Client(4L, null, null, 0, null))));

        Mockito.verify(orderRepository, Mockito.times(1))
                .getCategoryAndMostClient();
    }
}


