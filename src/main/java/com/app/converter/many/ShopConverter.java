package com.app.converter.many;

import com.app.controller.dto.order.GroupByDto;

import java.util.List;
import java.util.Map;

/**
 * Interface for converting data related to shops and statistics into different representations.
 * <p>
 * This interface defines a method for grouping shop-related data by age and the most popular categories.
 * </p>
 */
public interface ShopConverter {

    /**
     * Converts a map of ages and the associated most popular categories into a list of
     * {@link GroupByDto} objects.
     *
     * @param dtoList a map where the key is the age and the value is a list of most popular categories for that age
     * @return a list of {@link GroupByDto} objects, where each entry represents a group of categories
     * associated with an age
     */
    List<GroupByDto<Integer, String>> toAgeMostCategoryDto(Map<Integer, List<String>> dtoList);
}
