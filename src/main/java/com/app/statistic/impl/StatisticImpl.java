package com.app.statistic.impl;

import com.app.controller.dto.product.StatisticDto;
import com.app.converter.many.ProductsConverter;
import com.app.persistence.entity.view.PriceStatisticByCategoryProjection;
import com.app.model.Product;
import com.app.statistic.Statistic;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link Statistic} interface for storing and processing statistical data
 * regarding products, such as the minimum, maximum, and average prices for a given category.
 * This class is responsible for merging multiple statistics and converting them to DTO format for client-side consumption.
 */
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class StatisticImpl implements Statistic<Product, BigDecimal> {

    /**
     * The list of products with the minimum price in the given category.
     */
    @Getter
    final List<Product> min;

    /**
     * The list of products with the maximum price in the given category.
     */
    @Getter
    final List<Product> max;

    /**
     * The average price of products in the given category.
     */
    final BigDecimal avg;

    /**
     * Creates a new {@link StatisticImpl} instance from a {@link PriceStatisticByCategoryProjection} view projection.
     * This method maps the statistics from the projection to the corresponding product data.
     *
     * @param priceStatisticByCategoryProjection the projection containing statistical data for a product category
     * @return a new {@link StatisticImpl} object populated with data from the projection
     */
    public static Statistic<Product, BigDecimal> fromView(PriceStatisticByCategoryProjection priceStatisticByCategoryProjection) {
        return new StatisticImpl(
                new ArrayList<>(List.of(new Product(
                        priceStatisticByCategoryProjection.getMinId(), priceStatisticByCategoryProjection.getMinName(),
                        priceStatisticByCategoryProjection.getCategory(), priceStatisticByCategoryProjection.getMinPrice()))),
                new ArrayList<>(List.of(new Product(
                        priceStatisticByCategoryProjection.getMaxId(), priceStatisticByCategoryProjection.getMaxName(),
                        priceStatisticByCategoryProjection.getCategory(), priceStatisticByCategoryProjection.getMaxPrice()))),
                priceStatisticByCategoryProjection.getAvgPrice());
    }

    /**
     * Merges the current statistics with another set of statistics, combining the minimum and maximum products from both sets.
     *
     * @param statistic the {@link Statistic} object to merge with
     * @return the current {@link StatisticImpl} instance, with the merged data
     */
    public Statistic<Product, BigDecimal> merge(Statistic<Product, BigDecimal> statistic) {
        min.addAll(statistic.getMin());
        max.addAll(statistic.getMax());
        return this;
    }

    /**
     * Converts the statistical data into a {@link StatisticDto} object for easier consumption on the client side.
     *
     * @param productsConverterImpl the converter used to transform product entities into DTOs
     * @return a {@link StatisticDto} object containing the minimum, maximum, and average product data
     */
    public StatisticDto toStatisticDto(ProductsConverter productsConverterImpl) {
        return new StatisticDto(
                productsConverterImpl.toDtoList(min),
                productsConverterImpl.toDtoList(max),
                avg
        );
    }
}
