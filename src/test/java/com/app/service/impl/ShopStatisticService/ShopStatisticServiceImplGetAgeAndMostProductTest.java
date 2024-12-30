package com.app.service.impl.ShopStatisticService;


import com.app.model.Product;
import com.app.persistence.entity.view.AgeAndMostProductProjection;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ShopStatisticServiceImplGetAgeAndMostProductTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ShopStatisticServiceImpl shopStatisticService;

    @Test
    @DisplayName("When the repository returns an empty list, the service should also return an empty map.")
    public void test1() {

        Mockito.when(orderRepository.getAgeAndMostProduct())
                .thenReturn(List.of());

        Assertions.assertThat(shopStatisticService.getAgeAndMostProduct())
                .isEqualTo(Map.of());

        Mockito.verify(orderRepository, Mockito.times(1))
                .getAgeAndMostProduct();
    }

    @Test
    @DisplayName("When the repository returns one element of AgeAndMostProductProjection in the list, " +
            "the service should return a map with one record.")
    public void test2() {

        var projection = Mockito.mock(AgeAndMostProductProjection.class);

        Mockito.when(projection.getAge())
                        .thenReturn(10);

        Mockito.when(projection.getId())
                        .thenReturn(1L);

        Mockito.when(projection.getName())
                .thenReturn("A");

        Mockito.when(projection.getCategory())
                .thenReturn("C1");

        Mockito.when(projection.getPrice())
                .thenReturn(BigDecimal.ONE);

        Mockito.when(orderRepository.getAgeAndMostProduct())
                .thenReturn(List.of(projection));

        Assertions.assertThat(shopStatisticService.getAgeAndMostProduct())
                .isEqualTo(Map.of(10, List.of(new Product(1L, "A", "C1", BigDecimal.ONE))));

        Mockito.verify(orderRepository, Mockito.times(1))
                .getAgeAndMostProduct();
    }

    @Test
    @DisplayName("When the repository returns a list with two categories in one age group," +
            " the service should return a merged map.")
    public void test3() {

        var projection1 = Mockito.mock(AgeAndMostProductProjection.class);
        var projection2 = Mockito.mock(AgeAndMostProductProjection.class);

        Mockito.when(projection1.getAge())
                .thenReturn(10);

        Mockito.when(projection1.getId())
                .thenReturn(1L);

        Mockito.when(projection1.getName())
                .thenReturn("A");

        Mockito.when(projection1.getCategory())
                .thenReturn("C1");

        Mockito.when(projection1.getPrice())
                .thenReturn(BigDecimal.ONE);

        Mockito.when(projection2.getAge())
                .thenReturn(10);

        Mockito.when(projection2.getId())
                .thenReturn(2L);

        Mockito.when(projection2.getName())
                .thenReturn("B");

        Mockito.when(projection2.getCategory())
                .thenReturn("C2");

        Mockito.when(projection2.getPrice())
                .thenReturn(BigDecimal.ONE);

        Mockito.when(orderRepository.getAgeAndMostProduct())
                .thenReturn(List.of(projection1, projection2));

        Assertions.assertThat(shopStatisticService.getAgeAndMostProduct())
                .isEqualTo(Map.of(10, List.of(
                        new Product(1L, "A", "C1", BigDecimal.ONE),
                        new Product(2L, "B", "C2", BigDecimal.ONE))));

        Mockito.verify(orderRepository, Mockito.times(1))
                .getAgeAndMostProduct();
    }

    @Test
    @DisplayName("When the repository returns a list with several ages and products, it should return a correct map.")
    public void test4() {

        var projection1 = Mockito.mock(AgeAndMostProductProjection.class);
        var projection2 = Mockito.mock(AgeAndMostProductProjection.class);
        var projection3 = Mockito.mock(AgeAndMostProductProjection.class);
        var projection4 = Mockito.mock(AgeAndMostProductProjection.class);

        Mockito.when(projection1.getAge())
                .thenReturn(10);
        Mockito.when(projection1.getId())
                .thenReturn(1L);

        Mockito.when(projection2.getAge())
                .thenReturn(10);
        Mockito.when(projection2.getId())
                .thenReturn(2L);

        Mockito.when(projection3.getAge())
                .thenReturn(15);
        Mockito.when(projection3.getId())
                .thenReturn(3L);

        Mockito.when(projection4.getAge())
                .thenReturn(25);
        Mockito.when(projection4.getId())
                .thenReturn(4L);

        Mockito.when(orderRepository.getAgeAndMostProduct())
                .thenReturn(List.of(projection1, projection2, projection3, projection4));

        Assertions.assertThat(shopStatisticService.getAgeAndMostProduct())
                .isEqualTo(Map.of(
                        10, List.of(
                                new Product(1L, null, null, null)
                                ,new Product(2L, null, null, null)),
                        15,List.of(new Product(3L, null, null, null)),
                        25, List.of(new Product(4L, null, null, null))
                ));

        Mockito.verify(orderRepository, Mockito.times(1))
                .getAgeAndMostProduct();
    }
}


