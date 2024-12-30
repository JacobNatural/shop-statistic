package com.app.converter.many.impl;

import com.app.controller.dto.order.CategoryAndPriceStatisticDto;
import com.app.controller.dto.order.GroupByDto;
import com.app.controller.dto.product.ProductDto;
import com.app.converter.many.ProductsConverter;
import com.app.model.Product;
import com.app.persistence.entity.ProductEntity;
import com.app.statistic.Statistic;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link ProductsConverter} interface for converting
 * {@link Product} entities to {@link ProductDto} data transfer objects (DTOs)
 * and performing various transformations for product-related statistics.
 *
 * <p>This component is responsible for converting between different representations
 * of products, including DTOs, entities, and grouped statistics.</p>
 */
@Component
public class ProductsConverterImpl implements ProductsConverter {

    /**
     * Converts a list of {@link Product} entities to a list of {@link ProductDto} objects.
     *
     * @param products the list of {@link Product} entities to convert
     * @return a list of {@link ProductDto} objects representing the products
     */
    @Override
    public List<ProductDto> toDtoList(List<Product> products) {
        return products
                .stream()
                .map(Product::toProductDto)
                .toList();
    }

    /**
     * Converts a list of {@link ProductEntity} entities to a list of {@link Product} objects.
     *
     * @param productEntities the list of {@link ProductEntity} entities to convert
     * @return a list of {@link Product} objects representing the products
     */
    @Override
    public List<Product> toProduct(List<ProductEntity> productEntities) {
        return productEntities
                .stream()
                .map(ProductEntity::toProduct)
                .toList();
    }

    /**
     * Converts a map of product category statistics to a list of {@link CategoryAndPriceStatisticDto}.
     *
     * <p>The map contains category names as keys and {@link Statistic} objects as values.
     * Each {@link Statistic} is transformed into a {@link CategoryAndPriceStatisticDto}.</p>
     *
     * @param priceStatistic the map of product category statistics
     * @return a list of {@link CategoryAndPriceStatisticDto} objects representing price statistics
     */
    @Override
    public List<CategoryAndPriceStatisticDto> categoryAndPriceStatisticDto(Map<String, Statistic<Product, BigDecimal>> priceStatistic) {
        return priceStatistic
                .entrySet()
                .stream()
                .map(m -> new CategoryAndPriceStatisticDto(
                        m.getKey(), m.getValue().toStatisticDto(new ProductsConverterImpl())))
                .toList();
    }

    /**
     * Converts a list of {@link ProductDto} objects to a list of {@link ProductEntity} entities.
     *
     * @param products the list of {@link ProductDto} objects to convert
     * @return a list of {@link ProductEntity} entities representing the products
     */
    @Override
    public List<ProductEntity> toProductEntities(List<ProductDto> products) {
        return products
                .stream()
                .map(ProductDto::toProductEntity)
                .toList();
    }

    /**
     * Converts a map of product age groups to a list of {@link GroupByDto} containing
     * the age group as the key and a list of {@link ProductDto} objects as the value.
     *
     * @param map the map of product age groups
     * @return a list of {@link GroupByDto} representing age groups and their corresponding products
     */
    @Override
    public List<GroupByDto<Integer, ProductDto>> ageAndMostProductDto(Map<Integer, List<Product>> map) {
        return map
                .entrySet()
                .stream()
                .map(m -> new GroupByDto<>(m.getKey(), toDtoList(m.getValue())))
                .toList();
    }
}
