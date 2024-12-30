package com.app.controller;

import com.app.controller.dto.*;
import com.app.controller.dto.product.ProductDto;
import com.app.controller.dto.product.ProductsFilterDto;
import com.app.converter.many.ProductsConverter;
import com.app.service.ProductService;
import com.app.validator.impl.ProductDtoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing products in the application.
 * <p>
 * This class provides endpoints for creating, retrieving, and deleting products,
 * as well as filtering and managing product categories.
 * </p>
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ProductsConverter productsConverterImpl;

    @Qualifier("productDtoValidator")
    private final ProductDtoValidator productValidator;

    /**
     * Initializes the binder for the productDto to apply the custom productDtoValidator.
     *
     * @param binder the WebDataBinder for productDto.
     */
    @InitBinder("productDto")
    @Operation(
            description = "Access only for ADMIN and LEADER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    private void addProductInitBinder(WebDataBinder binder) {
        binder.setValidator(productValidator);
    }

    /**
     * Creates a new product.
     * <p>
     * This method accepts a {@link ProductDto} in the request body, validates it,
     * and creates a new product. If there are validation errors, it returns a bad request.
     * </p>
     *
     * @param productDto    the product data to be added.
     * @param bindingResult the result of the validation.
     * @return a {@link ResponseDto} containing the ID of the created product.
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            description = "Access only for ADMIN and LEADER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<Long> addProducts(@Valid @RequestBody ProductDto productDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getCode)
                    .collect(Collectors.joining("\n")));
        }
        return new ResponseDto<>(productService.addProducts(productDto));
    }

    /**
     * Creates multiple products.
     * <p>
     * This method accepts a list of {@link ProductDto} objects in the request body,
     * validates each product, and creates multiple products. If there are validation errors,
     * it returns a bad request.
     * </p>
     *
     * @param productsDto the list of products data to be added.
     * @return a {@link ResponseDto} containing the list of IDs of the created products.
     */
    @PostMapping("/all")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            description = "Access only for ADMIN and LEADER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<Long>> addProducts(@RequestBody List<ProductDto> productsDto) {
        productsDto.forEach(productDto -> {
            var errors = new BeanPropertyBindingResult(productDto, "productDto");
            productValidator.validate(productDto, errors);
            if (errors.hasErrors()) {
                throw new IllegalArgumentException(errors
                        .getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getCode)
                        .collect(Collectors.joining("\n")));
            }
        });

        return new ResponseDto<>(productService.addProducts(productsDto));
    }

    /**
     * Retrieves a product by its ID.
     * <p>
     * This method accepts the ID of the product as a path variable and returns the details
     * of the product in a {@link ProductDto}.
     * </p>
     *
     * @param id the ID of the product to be retrieved.
     * @return a {@link ResponseDto} containing the details of the found product.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    @Operation(
            description = "Access only for ADMIN and LEADER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<ProductDto> findById(@PathVariable Long id) {
        return new ResponseDto<>(productService
                .findById(id).toProductDto());
    }

    /**
     * Retrieves multiple products by their IDs.
     * <p>
     * This method accepts a list of product IDs as request parameters and returns the details
     * of the products in a list of {@link ProductDto}.
     * </p>
     *
     * @param ids the list of product IDs to be retrieved.
     * @return a {@link ResponseDto} containing a list of found products.
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.FOUND)
    @Operation(
            description = "Access only for ADMIN and LEADER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<ProductDto>> findAllByIds(@RequestParam List<Long> ids) {
        return new ResponseDto<>(productsConverterImpl
                .toDtoList(productService.findAllByIds(ids)));
    }

    /**
     * Removes a product by its ID.
     * <p>
     * This method accepts the ID of the product to be removed as a path variable and deletes
     * the product. It returns the ID of the removed product wrapped in a {@link ResponseDto}.
     * </p>
     *
     * @param id the ID of the product to be removed.
     * @return a {@link ResponseDto} containing the ID of the removed product.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access only for ADMIN and LEADER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<Long> removeById(@PathVariable Long id) {
        return new ResponseDto<>(productService.removeElement(id));
    }

    /**
     * Removes multiple products by their IDs.
     * <p>
     * This method accepts a list of product IDs as request parameters and deletes the products.
     * It returns the list of IDs of the removed products wrapped in a {@link ResponseDto}.
     * </p>
     *
     * @param ids the list of product IDs to be removed.
     * @return a {@link ResponseDto} containing a list of the IDs of the removed products.
     */
    @DeleteMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access only for ADMIN and LEADER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<Long>> removeAllByIds(@RequestParam List<Long> ids) {
        return new ResponseDto<>(productService.removeAllByIds(ids));
    }

    /**
     * Retrieves all product categories.
     * <p>
     * This method returns a list of all categories available for products.
     * </p>
     *
     * @return a {@link ResponseDto} containing a list of product categories.
     */
    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.FOUND)
    @Operation(
            description = "Access only for ADMIN and LEADER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<String>> getCategories() {
        return new ResponseDto<>(productService.getCategories());
    }

    /**
     * Filters products based on the provided criteria.
     * <p>
     * This method accepts a {@link ProductsFilterDto} object containing filter criteria,
     * and returns a list of products that match the criteria.
     * </p>
     *
     * @param productsFilterDto the filter criteria for products.
     * @return a {@link ResponseDto} containing the filtered list of products.
     */
    @PostMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access only for ADMIN and LEADER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<ProductDto>> filterProduct(@RequestBody ProductsFilterDto productsFilterDto) {
        return new ResponseDto<>(productsConverterImpl
                .toDtoList(productService.filterProducts(productsFilterDto)));
    }
}
