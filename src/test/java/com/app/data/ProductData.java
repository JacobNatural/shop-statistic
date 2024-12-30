package com.app.data;

import com.app.controller.dto.order.CategoryAndPriceStatisticDto;
import com.app.controller.dto.order.GroupByDto;
import com.app.controller.dto.product.ProductDto;
import com.app.controller.dto.product.StatisticDto;
import com.app.model.Product;
import com.app.persistence.entity.ProductEntity;
import com.app.statistic.Statistic;
import com.app.statistic.impl.StatisticImpl;

import java.math.BigDecimal;
import java.util.List;

public interface ProductData {
    Product PRODUCT1 = new Product(
            1L, "Apple", "groceries", BigDecimal.valueOf(2.3));

    Product PRODUCT2 = new Product(
            2L, "Banana", "groceries", BigDecimal.valueOf(1.7));

    Product PRODUCT3 = new Product(
            3L, "desk", "home", BigDecimal.valueOf(59.10));

    ProductDto PRODUCT_DTO1 = new ProductDto(
            1L, "Apple", "groceries", BigDecimal.valueOf(2.3));

    ProductDto PRODUCT_DTO2 = new ProductDto(
            2L, "Banana", "groceries", BigDecimal.valueOf(1.7));

    ProductDto PRODUCT_DTO3 = new ProductDto(
            3L, "desk", "home", BigDecimal.valueOf(59.10));

    ProductEntity PRODUCT_ENTITY_READ1 = ProductEntity
            .builder()
            .id(1L)
            .name("Apple")
            .category("groceries")
            .price(BigDecimal.valueOf(2.3))
            .build();

    ProductEntity PRODUCT_ENTITY_READ2 = ProductEntity
            .builder()
            .id(2L)
            .name("Banana")
            .category("groceries")
            .price(BigDecimal.valueOf(1.7))
            .build();

    ProductEntity PRODUCT_ENTITY_READ3 = ProductEntity
            .builder()
            .id(3L)
            .name("desk")
            .category("home")
            .price(BigDecimal.valueOf(59.10))
            .build();

    ProductEntity PRODUCT_ENTITY_READ4 = ProductEntity
            .builder()
            .id(4L)
            .name("table")
            .category("home")
            .price(BigDecimal.valueOf(59.10))
            .build();

    ProductEntity PRODUCT_ENTITY_1 = ProductEntity
            .builder()
            .name("Apple")
            .category("groceries")
            .price(BigDecimal.valueOf(2.3))
            .build();

    ProductEntity PRODUCT_ENTITY_2 = ProductEntity
            .builder()
            .name("Banana")
            .category("groceries")
            .price(BigDecimal.valueOf(1.7))
            .build();

    ProductEntity PRODUCT_ENTITY_3 = ProductEntity
            .builder()
            .name("desk")
            .category("home")
            .price(BigDecimal.valueOf(59.10))
            .build();

    Statistic<Product, BigDecimal> STATISTIC_1 = new StatisticImpl(
            List.of(PRODUCT1),List.of(PRODUCT2), BigDecimal.valueOf(2));

    Statistic<Product, BigDecimal> STATISTIC_2 = new StatisticImpl(
            List.of(PRODUCT3),List.of(PRODUCT3), BigDecimal.valueOf(59.10));

    StatisticDto STATISTIC_DTO1 = new StatisticDto(
            List.of(PRODUCT_DTO1), List.of(PRODUCT_DTO2), BigDecimal.valueOf(2));

    StatisticDto STATISTIC_DTO2 = new StatisticDto(
            List.of(PRODUCT_DTO3), List.of(PRODUCT_DTO3), BigDecimal.valueOf(59.10));

    CategoryAndPriceStatisticDto CATEGORY_AND_PRICE_STATISTIC_DTO1 =
            new CategoryAndPriceStatisticDto("groceries", STATISTIC_DTO1);

    CategoryAndPriceStatisticDto CATEGORY_AND_PRICE_STATISTIC_DTO2 =
            new CategoryAndPriceStatisticDto("home", STATISTIC_DTO2);

    GroupByDto<Integer, ProductDto> GROUP_BY_PRODUCT_DTO1 = new GroupByDto<>(
            20, List.of(PRODUCT_DTO1, PRODUCT_DTO2));

    GroupByDto<Integer, ProductDto> GROUP_BY_PRODUCT_DTO2 = new GroupByDto<>(
            29, List.of(PRODUCT_DTO3));



}
