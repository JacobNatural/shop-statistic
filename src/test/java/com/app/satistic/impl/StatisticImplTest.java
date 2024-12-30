package com.app.satistic.impl;


import com.app.controller.dto.product.StatisticDto;
import com.app.converter.many.ProductsConverter;
import com.app.model.Product;
import com.app.persistence.entity.view.PriceStatisticByCategoryProjection;
import com.app.statistic.impl.StatisticImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.app.data.ProductData.*;

public class StatisticImplTest {

    @Test
    @DisplayName("When convert from view to statistic.")
    public void test1() {
        var projection = Mockito.mock(PriceStatisticByCategoryProjection.class);
        Mockito.when(projection.getMinPrice()).thenReturn(BigDecimal.ONE);
        Mockito.when(projection.getMinId()).thenReturn(1L);
        Mockito.when(projection.getMinName()).thenReturn("A");
        Mockito.when(projection.getCategory()).thenReturn("g");
        Mockito.when(projection.getMaxPrice()).thenReturn(BigDecimal.TWO);
        Mockito.when(projection.getMaxId()).thenReturn(2L);
        Mockito.when(projection.getMaxName()).thenReturn("B");
        Mockito.when(projection.getAvgPrice()).thenReturn(BigDecimal.valueOf(1.5));

        Assertions.assertThat(StatisticImpl.fromView(projection))
                .isEqualTo(new StatisticImpl(
                        List.of(new Product(1L, "A", "g", BigDecimal.ONE)),
                        List.of(new Product(2L, "B", "g", BigDecimal.TWO)),
                        BigDecimal.valueOf(1.5)));
    }

    @Test
    @DisplayName("When merging two statistics with the same category, combine their data into one")
    public void test2() {

        var product1 = new Product(1L,"A", "g", BigDecimal.ONE);
        var product2 = new Product(1L,"B", "g", BigDecimal.TWO);
        var product3 = new Product(1L,"C", "g", BigDecimal.ONE);
        var product4 = new Product(1L,"D", "g", BigDecimal.TWO);

        var statistic1 = new StatisticImpl(new ArrayList<>(List.of(product1)),
                new ArrayList<>(List.of(product2)), BigDecimal.valueOf(1.5));
        var statistic2 = new StatisticImpl(
                new ArrayList<>(List.of(product3)),
                new ArrayList<>(List.of(product4)), BigDecimal.valueOf(1.5));

        Assertions.assertThat(statistic1.merge(statistic2))
                .isEqualTo(new StatisticImpl(
                        List.of(product1, product2),
                        List.of(product3, product4),
                        BigDecimal.valueOf(1.5)));
    }

    @Test
    @DisplayName("When convert statistic to statistic dto.")
    public void test3(){

        var productConverter = Mockito.mock(ProductsConverter.class);

        Mockito.when(productConverter.toDtoList(List.of(PRODUCT1)))
                .thenReturn(List.of(PRODUCT_DTO1));

        Mockito.when(productConverter.toDtoList(List.of(PRODUCT2)))
                .thenReturn(List.of(PRODUCT_DTO2));

        var statistic = new StatisticImpl(
                List.of(PRODUCT1), List.of(PRODUCT2), BigDecimal.valueOf(2));

        Assertions.assertThat(statistic.toStatisticDto(productConverter))
                .isEqualTo(new StatisticDto(
                        List.of(PRODUCT_DTO1),
                        List.of(PRODUCT_DTO2),
                        BigDecimal.valueOf(2)));

    }
}
