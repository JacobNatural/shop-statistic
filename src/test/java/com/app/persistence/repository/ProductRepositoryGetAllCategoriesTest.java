package com.app.persistence.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.List;

import static com.app.data.ProductData.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class ProductRepositoryGetAllCategoriesTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("When we don't have products in the database and return an empty list.")
    public void test1() {

        Assertions.assertThat(productRepository.getAllCategories())
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When we have the same category twice and return only one category.")
    public void test2() {

        productRepository.saveAll(List.of(PRODUCT_ENTITY_1, PRODUCT_ENTITY_2));

        Assertions.assertThat(productRepository.getAllCategories())
                .contains("groceries");
    }

    @Test
    @DisplayName("When we have multiple categories and return all available categories.")
    public void test3() {

        productRepository.saveAll(List.of(PRODUCT_ENTITY_1, PRODUCT_ENTITY_2, PRODUCT_ENTITY_3));

        Assertions.assertThat(productRepository.getAllCategories())
                .contains("groceries", "home");
    }


}
