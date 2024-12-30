package com.app.validator.impl;

import com.app.controller.dto.ClientDto;
import com.app.controller.dto.product.ProductDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Error.class, Validator.class, ProductDtoValidator.class})
public class ProductDtoValidatorTest {

    @Autowired
    private ProductDtoValidator productDtoValidator;

    @Value("${validate.product.name.regex}")
    private String nameRegex;
    @Value("${validate.product.name.min.length}")
    private int nameMinLength;
    @Value("${validate.product.category.regex}")
    private String categoryRegex;
    @Value("${validate.product.category.min.length}")
    private int categoryMinLength;
    @Value("${validate.product.min.price}")
    private BigDecimal minPrice;


    @Test
    @DisplayName("When the product name is null.")
    public void test1() {

        var productDto = new ProductDto(1L, null, "groceries", BigDecimal.valueOf(50.5));
        Errors errors = new BeanPropertyBindingResult(productDto, "productDto");

        productDtoValidator.validate(productDto, errors);

        Assertions.assertThat(errors.hasFieldErrors("name")).isTrue();
        Assertions.assertThat(errors.getFieldError("name").getCode())
                .isEqualTo("Product name cannot be null");
    }

    @Test
    @DisplayName("When the product name is too short.")
    public void test2() {
        var productDto = new ProductDto(1L, "a", "groceries", BigDecimal.valueOf(50.5));
        Errors errors = new BeanPropertyBindingResult(productDto, "productDto");

        productDtoValidator.validate(productDto, errors);

        Assertions.assertThat(errors.hasFieldErrors("name")).isTrue();
        Assertions.assertThat(errors.getFieldError("name").getCode())
                .isEqualTo("Product name must be at least " + nameMinLength + " characters");
    }

    @Test
    @DisplayName("When the product name contains illegal characters.")
    public void test3() {
        var productDto = new ProductDto(1L, "Jakub!", "groceries", BigDecimal.valueOf(50.5));
        Errors errors = new BeanPropertyBindingResult(productDto, "productDto");

        productDtoValidator.validate(productDto, errors);

        Assertions.assertThat(errors.hasFieldErrors("name")).isTrue();
        Assertions.assertThat(errors.getFieldError("name").getCode())
                .isEqualTo("Product name contains invalid characters");
    }

    @Test
    @DisplayName("When the product category is null.")
    public void test4() {
        var productDto = new ProductDto(1L, "apple", null, BigDecimal.valueOf(50.5));
        Errors errors = new BeanPropertyBindingResult(productDto, "productDto");

        productDtoValidator.validate(productDto, errors);

        Assertions.assertThat(errors.hasFieldErrors("category")).isTrue();
        Assertions.assertThat(errors.getFieldError("category").getCode())
                .isEqualTo("Product category cannot be null");
    }

    @Test
    @DisplayName("When the product category is too short.")
    public void test5() {
        var productDto = new ProductDto(1L, "apple", "c", BigDecimal.valueOf(50.5));
        Errors errors = new BeanPropertyBindingResult(productDto, "productDto");

        productDtoValidator.validate(productDto, errors);

        Assertions.assertThat(errors.hasFieldErrors("category")).isTrue();
        Assertions.assertThat(errors.getFieldError("category").getCode())
                .isEqualTo("Product category must be at least " + categoryMinLength + " characters");
    }

    @Test
    @DisplayName("When the product category contains illegal characters.")
    public void test6() {
        var productDto = new ProductDto(1L, "apple", "groceries@", BigDecimal.valueOf(50.5));
        Errors errors = new BeanPropertyBindingResult(productDto, "productDto");

        productDtoValidator.validate(productDto, errors);

        Assertions.assertThat(errors.hasFieldErrors("category")).isTrue();
        Assertions.assertThat(errors.getFieldError("category").getCode())
                .isEqualTo("Product category contains invalid characters");
    }

    @Test
    @DisplayName("When the product price is null.")
    public void test7() {
        var productDto = new ProductDto(1L, "apple", "groceries", null);
        Errors errors = new BeanPropertyBindingResult(productDto, "productDto");

        productDtoValidator.validate(productDto, errors);

        Assertions.assertThat(errors.hasFieldErrors("price")).isTrue();
        Assertions.assertThat(errors.getFieldError("price").getCode())
                .isEqualTo("Price cannot be null");

    }

    @Test
    @DisplayName("When the product price zero.")
    public void test8() {
        var productDto = new ProductDto(1L, "apple", "groceries", BigDecimal.ZERO);
        Errors errors = new BeanPropertyBindingResult(productDto, "productDto");

        productDtoValidator.validate(productDto, errors);

        Assertions.assertThat(errors.hasFieldErrors("price")).isTrue();
        Assertions.assertThat(errors.getFieldError("price").getCode())
                .isEqualTo("Product price must be greater than " + minPrice);
    }

    @Test
    @DisplayName("When the product data are correct.")
    public void test9() {
        var productDto = new ProductDto(1L, "apple", "groceries", BigDecimal.TEN);
        Errors errors = new BeanPropertyBindingResult(productDto, "productDto");

        productDtoValidator.validate(productDto, errors);
        productDtoValidator.supports(productDto.getClass());

        Assertions.assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    @DisplayName("When try validate another object.")
    public void test10() {
        var clientDto = new ClientDto(1L, "A", "B", 10, BigDecimal.valueOf(10));
        Errors errors = new BeanPropertyBindingResult(clientDto, "clientDto");

        productDtoValidator.validate(clientDto, errors);
        productDtoValidator.supports(clientDto.getClass());

        Assertions.assertThat(errors.hasErrors()).isFalse();
    }


}
