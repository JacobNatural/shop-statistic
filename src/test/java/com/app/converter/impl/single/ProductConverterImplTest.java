package com.app.converter.impl.single;

import com.app.converter.single.Converter;
import com.app.converter.single.impl.ProductConverterImpl;
import com.app.model.Product;
import com.app.persistence.entity.ProductEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.app.data.ProductData.PRODUCT1;
import static com.app.data.ProductData.PRODUCT_ENTITY_READ1;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ProductConverterImpl.class)
public class ProductConverterImplTest {

    @Autowired
    private Converter<ProductEntity, Product> converter;

    @Test
    @DisplayName("When convert product entity to product.")
    public void test1(){
        Assertions.assertThat(converter.toModel(PRODUCT_ENTITY_READ1))
                .isEqualTo(PRODUCT1);
    }

    @Test
    @DisplayName("When convert client to entity client.")
    public void test2(){
        Assertions.assertThat(converter.toEntity(PRODUCT1))
                .isEqualTo(PRODUCT_ENTITY_READ1);
    }
}

