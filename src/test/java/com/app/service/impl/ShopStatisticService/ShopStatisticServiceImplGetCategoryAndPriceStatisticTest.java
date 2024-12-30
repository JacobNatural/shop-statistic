package com.app.service.impl.ShopStatisticService;


import com.app.model.Product;
import com.app.persistence.entity.view.PriceStatisticByCategoryProjection;
import com.app.persistence.repository.ProductRepository;
import com.app.service.impl.ShopStatisticServiceImpl;
import com.app.statistic.impl.StatisticImpl;
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
public class ShopStatisticServiceImplGetCategoryAndPriceStatisticTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ShopStatisticServiceImpl shopStatisticService;

    @Test
    @DisplayName("When the repository returns an empty list, the service should also return an empty map.")
    public void test1() {

        Mockito.when(productRepository.getCategoryAndPriceStatistic())
                .thenReturn(List.of());

        Assertions.assertThat(shopStatisticService.getCategoryAndPriceStatistic())
                .isEqualTo(Map.of());

        Mockito.verify(productRepository, Mockito.times(1))
                .getCategoryAndPriceStatistic();
    }

    @Test
    @DisplayName("When the repository returns one element of PriceStatisticByCategoryProjection in the list, " +
            "the service should return a map with one record.")
    public void test2() {

        var projection = Mockito.mock(PriceStatisticByCategoryProjection.class);

        Mockito.when(projection.getCategory())
                .thenReturn("home");

        Mockito.when(projection.getMinId())
                .thenReturn(1L);

        Mockito.when(projection.getMaxId())
                .thenReturn(1L);


        Mockito.when(productRepository.getCategoryAndPriceStatistic())
                .thenReturn(List.of(projection));

        Assertions.assertThat(shopStatisticService.getCategoryAndPriceStatistic())
                .isEqualTo(Map.of("home", new StatisticImpl(
                        List.of(new Product(1L, null, null, null)),
                        List.of(new Product(1L, null, null, null)), null)
                ));

        Mockito.verify(productRepository, Mockito.times(1))
                .getCategoryAndPriceStatistic();
    }

    @Test
    @DisplayName("When the repository returns a list containing two categories and price statistics, " +
            "the service should return a merged map.")
    public void test3() {

        var projection1 = Mockito.mock(PriceStatisticByCategoryProjection.class);
        var projection2 = Mockito.mock(PriceStatisticByCategoryProjection.class);

        Mockito.when(projection1.getCategory())
                .thenReturn("home");

        Mockito.when(projection1.getMinId())
                .thenReturn(1L);

        Mockito.when(projection1.getMaxId())
                .thenReturn(2L);

        Mockito.when(projection2.getCategory())
                .thenReturn("groceries");

        Mockito.when(projection2.getMinId())
                .thenReturn(3L);

        Mockito.when(projection2.getMaxId())
                .thenReturn(4L);

        Mockito.when(productRepository.getCategoryAndPriceStatistic())
                .thenReturn(List.of(projection1, projection2));

        Assertions.assertThat(shopStatisticService.getCategoryAndPriceStatistic())
                .isEqualTo(Map.of(
                        "home", new StatisticImpl(
                                List.of(new Product(1L, null, null, null)),
                                List.of(new Product(2L, null, null, null)), null),
                        "groceries", new StatisticImpl(
                                List.of(new Product(3L, null, null, null)),
                                List.of(new Product(4L, null, null, null)), null)
                ));

        Mockito.verify(productRepository, Mockito.times(1))
                .getCategoryAndPriceStatistic();
    }
}


