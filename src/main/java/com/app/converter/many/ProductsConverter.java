package com.app.converter.many;

import com.app.controller.dto.order.CategoryAndPriceStatisticDto;
import com.app.controller.dto.order.GroupByDto;
import com.app.controller.dto.product.ProductDto;
import com.app.model.Product;
import com.app.persistence.entity.ProductEntity;
import com.app.statistic.Statistic;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Interface for converting a list of {@link Product} entities to their corresponding {@link ProductDto}
 * representations and for performing other conversion tasks related to products and statistics.
 * <p>
 * This interface provides methods to convert {@link Product} entities to DTOs, generate product
 * statistics, and map between different representations of products.
 * </p>
 */
public interface ProductsConverter {

    /**
     * Converts a list of {@link Product} entities to a list of {@link ProductDto} objects.
     *
     * @param products the list of {@link Product} objects to convert
     * @return a list of {@link ProductDto} objects representing the products
     */
    List<ProductDto> toDtoList(List<Product> products);

    /**
     * Converts a map of product categories and associated statistics to a list of
     * {@link CategoryAndPriceStatisticDto} objects.
     *
     * @param priceStatistic a map of product categories and their corresponding statistics
     * @return a list of {@link CategoryAndPriceStatisticDto} objects representing the price statistics
     */
    List<CategoryAndPriceStatisticDto> categoryAndPriceStatisticDto(Map<String, Statistic<Product, BigDecimal>> priceStatistic);

    /**
     * Converts a list of {@link ProductDto} objects to a list of {@link ProductEntity} objects.
     *
     * @param products the list of {@link ProductDto} objects to convert
     * @return a list of {@link ProductEntity} objects representing the products
     */
    List<ProductEntity> toProductEntities(List<ProductDto> products);

    /**
     * Converts a map of product ages to the most popular products into a list of
     * {@link GroupByDto} objects.
     *
     * @param map a map where the key is the product age and the value is a list of popular products
     * @return a list of {@link GroupByDto} objects representing products grouped by age
     */
    List<GroupByDto<Integer, ProductDto>> ageAndMostProductDto(Map<Integer, List<Product>> map);

    /**
     * Converts a list of {@link ProductEntity} objects to a list of {@link Product} objects.
     *
     * @param productEntities the list of {@link ProductEntity} objects to convert
     * @return a list of {@link Product} objects representing the products
     */
    List<Product> toProduct(List<ProductEntity> productEntities);
}
