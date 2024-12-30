package com.app.service.impl.ShopStatisticService;


import com.app.persistence.entity.view.AgeMostOftenCategoryProjection;
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
import java.util.Map;


@ExtendWith(MockitoExtension.class)
public class ShopStatisticServiceImplGetAgeAndMostCategoryTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ShopStatisticServiceImpl shopStatisticService;

    @Test
    @DisplayName("When the repository returns an empty list, the service should also return an empty map.")
    public void test1() {

        Mockito.when(orderRepository.getAgeAndMostCategory())
                .thenReturn(List.of());

        Assertions.assertThat(shopStatisticService.getAgeAndMostCategory())
                .isEqualTo(Map.of());

        Mockito.verify(orderRepository, Mockito.times(1))
                .getAgeAndMostCategory();
    }

    @Test
    @DisplayName("When the repository returns one element of AgeMostOftenCategoryProjection in the list, " +
            "the service should return a map with one record.")
    public void test2() {

        var projection = Mockito.mock(AgeMostOftenCategoryProjection.class);

        Mockito.when(projection.getAge())
                        .thenReturn(10);

        Mockito.when(projection.getCategory())
                        .thenReturn("home");

        Mockito.when(orderRepository.getAgeAndMostCategory())
                .thenReturn(List.of(projection));

        Assertions.assertThat(shopStatisticService.getAgeAndMostCategory())
                .isEqualTo(Map.of(10, List.of("home")));

        Mockito.verify(orderRepository, Mockito.times(1))
                .getAgeAndMostCategory();
    }

    @Test
    @DisplayName("When the repository returns a list with two categories in one age group," +
            " the service should return a merged map.")
    public void test3() {

        var projection1 = Mockito.mock(AgeMostOftenCategoryProjection.class);
        var projection2 = Mockito.mock(AgeMostOftenCategoryProjection.class);

        Mockito.when(projection1.getAge())
                .thenReturn(10);

        Mockito.when(projection1.getCategory())
                .thenReturn("home");

        Mockito.when(projection2.getAge())
                .thenReturn(10);

        Mockito.when(projection2.getCategory())
                .thenReturn("groceries");

        Mockito.when(orderRepository.getAgeAndMostCategory())
                .thenReturn(List.of(projection1, projection2));

        Assertions.assertThat(shopStatisticService.getAgeAndMostCategory())
                .isEqualTo(Map.of(10, List.of("home", "groceries")));

        Mockito.verify(orderRepository, Mockito.times(1))
                .getAgeAndMostCategory();
    }

    @Test
    @DisplayName("When the repository returns a list with several ages and categories, it should return a correct map.")
    public void test4() {

        var projection1 = Mockito.mock(AgeMostOftenCategoryProjection.class);
        var projection2 = Mockito.mock(AgeMostOftenCategoryProjection.class);
        var projection3 = Mockito.mock(AgeMostOftenCategoryProjection.class);
        var projection4 = Mockito.mock(AgeMostOftenCategoryProjection.class);

        Mockito.when(projection1.getAge())
                .thenReturn(10);

        Mockito.when(projection1.getCategory())
                .thenReturn("home");

        Mockito.when(projection2.getAge())
                .thenReturn(10);

        Mockito.when(projection2.getCategory())
                .thenReturn("groceries");

        Mockito.when(projection3.getAge())
                .thenReturn(15);

        Mockito.when(projection3.getCategory())
                .thenReturn("automotive");

        Mockito.when(projection4.getAge())
                .thenReturn(25);

        Mockito.when(projection4.getCategory())
                .thenReturn("garden");

        Mockito.when(orderRepository.getAgeAndMostCategory())
                .thenReturn(List.of(projection1, projection2, projection3, projection4));

        Assertions.assertThat(shopStatisticService.getAgeAndMostCategory())
                .isEqualTo(Map.of(
                        10, List.of("home", "groceries"),
                        15,List.of("automotive"),
                        25, List.of("garden")
                ));

        Mockito.verify(orderRepository, Mockito.times(1))
                .getAgeAndMostCategory();
    }
}


