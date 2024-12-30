package com.app.controller.dto.order;

import com.app.controller.dto.product.StatisticDto;

/**
 * A DTO that represents the statistics of a product category and its associated price statistics.
 * <p>
 * This class contains the name of the category and the corresponding statistics related to it.
 * </p>
 */
public record CategoryAndPriceStatisticDto(
        /**
         * The name of the product category.
         */
        String category,

        /**
         * The statistics related to the category's price.
         */
        StatisticDto statisticDto) {
}
