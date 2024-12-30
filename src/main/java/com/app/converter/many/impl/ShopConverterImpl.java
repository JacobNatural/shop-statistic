package com.app.converter.many.impl;

import com.app.controller.dto.order.GroupByDto;
import com.app.converter.many.ShopConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link ShopConverter} interface for converting
 * data related to shop statistics into appropriate DTOs (Data Transfer Objects).
 *
 * <p>This component handles the transformation of statistics regarding age and
 * category groupings into structured DTOs for further processing or API responses.</p>
 */
@Component
public class ShopConverterImpl implements ShopConverter {

    /**
     * Converts a map of age groups and associated categories into a list of
     * {@link GroupByDto} objects. Each {@link GroupByDto} contains an age group
     * as the key and a list of associated categories as the value.
     *
     * @param dtoList the map of age groups to categories
     * @return a list of {@link GroupByDto} objects, each representing an age group
     * and its associated categories
     */
    @Override
    public List<GroupByDto<Integer, String>> toAgeMostCategoryDto(Map<Integer, List<String>> dtoList) {
        return dtoList
                .entrySet()
                .stream()
                .map(map -> new GroupByDto<>(map.getKey(), map.getValue()))
                .toList();
    }
}
