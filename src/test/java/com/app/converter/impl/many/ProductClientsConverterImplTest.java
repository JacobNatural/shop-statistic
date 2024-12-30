package com.app.converter.impl.many;

import com.app.converter.many.ProductsConverter;
import com.app.converter.many.impl.ProductsConverterImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static com.app.data.ProductData.*;

@SpringBootTest(classes = ProductsConverterImpl.class)
public class ProductClientsConverterImplTest {

    @Autowired
    private ProductsConverter productsConverter;

    @Test
    @DisplayName("When converting an empty list of products to an empty list of product DTOs.")
    public void test1() {
        Assertions.assertThat(productsConverter.toDtoList(List.of()))
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When converting a list of products to a list of product DTOs.")
    public void test2() {
        Assertions.assertThat(productsConverter.toDtoList(List.of(
                        PRODUCT1, PRODUCT2, PRODUCT3
                )))
                .contains(PRODUCT_DTO1, PRODUCT_DTO2, PRODUCT_DTO3);
    }

    @Test
    @DisplayName("When converting an empty list of product entities to an empty list of products.")
    public void test3() {
        Assertions.assertThat(productsConverter.toProduct(List.of()))
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When converting a list of product entities to a list of products.")
    public void test4() {
        Assertions.assertThat(productsConverter.toProduct(List.of(
                        PRODUCT_ENTITY_READ1, PRODUCT_ENTITY_READ2, PRODUCT_ENTITY_READ3)))
                .contains(PRODUCT1, PRODUCT2, PRODUCT3);
    }

    @Test
    @DisplayName("When converting an empty map of category and price statistic to an empty list of CategoryAndPriceStatisticDto.")
    public void test5() {
        Assertions.assertThat(productsConverter.categoryAndPriceStatisticDto(Map.of()))
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When converting a map of category and price statistic to a list of CategoryAndPriceStatisticDto.")
    public void test6() {
        Assertions.assertThat(productsConverter.categoryAndPriceStatisticDto(Map.of(
                        "groceries", STATISTIC_1,
                        "home", STATISTIC_2)))
                .contains(CATEGORY_AND_PRICE_STATISTIC_DTO1, CATEGORY_AND_PRICE_STATISTIC_DTO2);
    }

    @Test
    @DisplayName("When converting an empty list of productsDto to an empty list of product entities.")
    public void test7() {
        Assertions.assertThat(productsConverter.toProductEntities(List.of()))
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When converting a list of productsDto to a list of product entities.")
    public void test8() {
        Assertions.assertThat(productsConverter.toProductEntities(List.of(
                        PRODUCT_DTO1, PRODUCT_DTO2, PRODUCT_DTO3
                )))
                .containsExactlyInAnyOrder(PRODUCT_ENTITY_1, PRODUCT_ENTITY_2, PRODUCT_ENTITY_3);
    }

    @Test
    @DisplayName("When converting an empty map of age and products to an empty list of groupByDto.")
    public void test9() {
        Assertions.assertThat(productsConverter.ageAndMostProductDto(Map.of()))
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When converting a map of age and products to a list of groupByDto.")
    public void test10() {
        Assertions.assertThat(productsConverter.ageAndMostProductDto(Map.of(
                20, List.of(PRODUCT1, PRODUCT2),
                29, List.of(PRODUCT3)
        ))).contains(GROUP_BY_PRODUCT_DTO1, GROUP_BY_PRODUCT_DTO2);
    }
}

