package com.app.service;

import com.app.controller.dto.product.ProductDto;
import com.app.controller.dto.product.ProductsFilterDto;
import com.app.model.Product;

import java.util.List;

/**
 * Interface for managing products. Extends the {@link CrudService} interface to inherit basic CRUD operations
 * for {@link Product} entities. This service provides additional methods for adding products, filtering products,
 * and retrieving product categories.
 */
public interface ProductService extends CrudService<Product> {

    /**
     * Adds a new product.
     *
     * @param productDto the DTO containing the product data to be added
     * @return the ID of the newly added product
     */
    Long addProducts(ProductDto productDto);

    /**
     * Adds multiple products.
     *
     * @param productsDto the list of DTOs containing the product data to be added
     * @return a list of IDs of the newly added products
     */
    List<Long> addProducts(List<ProductDto> productsDto);

    /**
     * Retrieves a list of all product categories.
     *
     * @return a list of product category names
     */
    List<String> getCategories();

    /**
     * Retrieves a list of products by their category.
     *
     * @param category the category of products to be retrieved
     * @return a list of products belonging to the specified category
     */
    List<Product> getProductsByCategory(String category);

    /**
     * Filters products based on the provided filter criteria.
     *
     * @param productsFilterDto the DTO containing the filter criteria
     * @return a list of products that match the filter criteria
     */
    List<Product> filterProducts(ProductsFilterDto productsFilterDto);
}
