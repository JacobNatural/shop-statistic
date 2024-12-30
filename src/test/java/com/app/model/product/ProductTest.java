package com.app.model.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.app.data.ProductData.*;

public class ProductTest {

    @Test
    @DisplayName("When converting a product to a productDto.")
    public void test1(){
        Assertions.assertThat(PRODUCT1.toProductDto())
                .isEqualTo(PRODUCT_DTO1);
    }

    @Test
    @DisplayName("When converting a product to a product entity.")
    public void test2(){
        Assertions.assertThat(PRODUCT1.toProductEntity())
                .isEqualTo(PRODUCT_ENTITY_READ1);
    }
}
