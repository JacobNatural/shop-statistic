package com.app.validator.impl;

import com.app.controller.dto.product.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

/**
 * Validator for validating {@link ProductDto} objects.
 * Ensures that the product name, category, and price meet the specified validation rules.
 */
@Component
@RequiredArgsConstructor
public class ProductDtoValidator implements Validator {

    /**
     * Regular expression to validate the product name.
     */
    @Value("${validate.product.name.regex}")
    private String nameRegex;

    /**
     * Minimum length for the product name.
     */
    @Value("${validate.product.name.min.length}")
    private int nameMinLength;

    /**
     * Regular expression to validate the product category.
     */
    @Value("${validate.product.category.regex}")
    private String categoryRegex;

    /**
     * Minimum length for the product category.
     */
    @Value("${validate.product.category.min.length}")
    private int categoryMinLength;

    /**
     * Minimum acceptable price for the product.
     */
    @Value("${validate.product.min.price}")
    private BigDecimal minPrice;

    /**
     * {@inheritDoc}
     * This method checks if the given class is {@link ProductDto}.
     *
     * @param clazz the class to check
     * @return true if the class is {@link ProductDto}, false otherwise
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(ProductDto.class);
    }

    /**
     * {@inheritDoc}
     * This method performs validation on the given {@link ProductDto} object.
     * It validates the product's name, category, and price according to the specified rules.
     *
     * @param target the object to validate
     * @param errors the {@link Errors} object to hold validation errors
     */
    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof ProductDto product) {
            validateName(product.name(), errors);
            validateCategory(product.category(), errors);
            validatePrice(product.price(), errors);
        }
    }

    /**
     * Validates the product's name.
     * - Checks if the name is not null.
     * - Checks if the name meets the minimum length requirement.
     * - Checks if the name matches the configured regular expression.
     *
     * @param name the name of the product
     * @param errors the {@link Errors} object to hold validation errors
     */
    private void validateName(String name, Errors errors) {
        if (stringNullCheck(name)) {
            errors.rejectValue("name", "Product name cannot be null");
            return;
        }
        if (stringMinLength(name, nameMinLength)) {
            errors.rejectValue("name", "Product name must be at least " + nameMinLength + " characters");
            return;
        }
        if (!stringRegexCheck(name, nameRegex)) {
            errors.rejectValue("name", "Product name contains invalid characters");
        }
    }

    /**
     * Validates the product's category.
     * - Checks if the category is not null.
     * - Checks if the category meets the minimum length requirement.
     * - Checks if the category matches the configured regular expression.
     *
     * @param category the category of the product
     * @param errors the {@link Errors} object to hold validation errors
     */
    private void validateCategory(String category, Errors errors) {
        if (stringNullCheck(category)) {
            errors.rejectValue("category", "Product category cannot be null");
            return;
        }
        if (stringMinLength(category, categoryMinLength)) {
            errors.rejectValue("category", "Product category must be at least " + categoryMinLength + " characters");
            return;
        }
        if (!stringRegexCheck(category, categoryRegex)) {
            errors.rejectValue("category", "Product category contains invalid characters");
        }
    }

    /**
     * Validates the product's price.
     * - Checks if the price is not null.
     * - Checks if the price is greater than the configured minimum price.
     *
     * @param price the price of the product
     * @param errors the {@link Errors} object to hold validation errors
     */
    private void validatePrice(BigDecimal price, Errors errors) {
        if (price == null) {
            errors.rejectValue("price", "Price cannot be null");
            return;
        }
        if (price.compareTo(minPrice) <= 0) {
            errors.rejectValue("price", "Product price must be greater than " + minPrice);
        }
    }

    /**
     * Checks if the given string is null.
     *
     * @param s the string to check
     * @return true if the string is null, false otherwise
     */
    private boolean stringNullCheck(String s) {
        return s == null;
    }

    /**
     * Checks if the given string's length is less than the specified minimum length.
     *
     * @param s the string to check
     * @param minLength the minimum length
     * @return true if the string's length is less than the minimum length, false otherwise
     */
    private boolean stringMinLength(String s, int minLength) {
        return s.length() < minLength;
    }

    /**
     * Checks if the given string matches the specified regular expression.
     *
     * @param s the string to check
     * @param regex the regular expression
     * @return true if the string matches the regex, false otherwise
     */
    private boolean stringRegexCheck(String s, String regex) {
        return s.matches(regex);
    }
}
