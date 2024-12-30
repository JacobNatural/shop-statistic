package com.app.converter.impl.many;

import com.app.controller.dto.order.GroupByDto;
import com.app.converter.many.ShopConverter;
import com.app.converter.many.impl.ShopConverterImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest(classes = ShopConverterImpl.class)
public class ShopClientsConverterImplTest {

    @Autowired
    private ShopConverter shopConverter;

    @Test
    @DisplayName("When converting an empty map of age and categories to an empty list of GroupByDto.")
    public void test1() {
        Assertions.assertThat(shopConverter.toAgeMostCategoryDto(Map.of()))
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When converting a map of category and price statistic to a list of GroupByDto.")
    public void test2() {
        Assertions.assertThat(shopConverter.toAgeMostCategoryDto(Map.of(
                10, List.of("groceries", "home"),
                20, List.of("automotive")
                )))
                .contains(
                        new GroupByDto<>(10,List.of("groceries", "home")),
                        new GroupByDto<>(20,List.of("automotive")));
    }
}
