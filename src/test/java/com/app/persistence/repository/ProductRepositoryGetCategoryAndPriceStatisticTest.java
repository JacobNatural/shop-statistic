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

import java.math.BigDecimal;
import java.util.List;

import static com.app.data.ProductData.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class ProductRepositoryGetCategoryAndPriceStatisticTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("When we don't have products in the database and return an empty list.")
    public void test1() {

        Assertions.assertThat(productRepository.getCategoryAndPriceStatistic())
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When we have only one product and return correct price statistic by category")
    public void test2() {

        productRepository.saveAll(List.of(PRODUCT_ENTITY_1));

        var pro = productRepository.getCategoryAndPriceStatistic();
        Assertions.assertThat(pro.getFirst().getCategory()).isEqualTo("groceries");
        Assertions.assertThat(pro.getFirst().getMinPrice()).isEqualByComparingTo(BigDecimal.valueOf(2.3));
        Assertions.assertThat(pro.getFirst().getMaxPrice()).isEqualByComparingTo(BigDecimal.valueOf(2.3));
        Assertions.assertThat(pro.getFirst().getAvgPrice()).isEqualByComparingTo(BigDecimal.valueOf(2.3));

    }

    @Test
    @DisplayName("When we have multipli products and return price statistic by category")
    public void test3() {

        productRepository.saveAll(List.of(PRODUCT_ENTITY_1, PRODUCT_ENTITY_2, PRODUCT_ENTITY_3));

        var projection = productRepository.getCategoryAndPriceStatistic();

        Assertions.assertThat(projection).hasSize(2);
        Assertions.assertThat(projection.getFirst().getCategory()).isEqualTo("groceries");
        Assertions.assertThat(projection.getFirst().getMinPrice()).isEqualByComparingTo(BigDecimal.valueOf(1.7));
        Assertions.assertThat(projection.getFirst().getMaxPrice()).isEqualByComparingTo(BigDecimal.valueOf(2.3));
        Assertions.assertThat(projection.getFirst().getAvgPrice()).isEqualByComparingTo(BigDecimal.valueOf(2));
        Assertions.assertThat(projection.get(1).getCategory()).isEqualTo("home");
        Assertions.assertThat(projection.get(1).getMinPrice()).isEqualByComparingTo(BigDecimal.valueOf(59.1));
        Assertions.assertThat(projection.get(1).getMaxPrice()).isEqualByComparingTo(BigDecimal.valueOf(59.1));
        Assertions.assertThat(projection.get(1).getAvgPrice()).isEqualByComparingTo(BigDecimal.valueOf(59.1));
    }

}
