package com.app.service.impl.crud;

import com.app.controller.dto.product.ProductDto;
import com.app.controller.dto.product.ProductsFilterDto;
import com.app.converter.single.Converter;
import com.app.converter.many.ProductsConverter;
import com.app.exception.ResourceAlreadyExistException;
import com.app.model.Product;
import com.app.persistence.entity.ProductEntity;
import com.app.persistence.repository.CrudRepository;
import com.app.persistence.repository.ProductRepository;
import com.app.persistence.repository.specification.ProductSpecification;
import com.app.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the {@link ProductService} interface that provides CRUD operations for managing products.
 * This service class interacts with the product repository, handles validation, and supports filtering and categorization
 * of products.
 * <p>
 * The service methods ensure that products are not duplicated based on name and category, and support filtering
 * based on the provided {@link ProductsFilterDto}.
 * </p>
 */
@Transactional
@Service
public class ProductServiceImpl extends GenericServiceImpl<ProductEntity, Product> implements ProductService {
    private final ProductRepository productRepository;
    private final ProductsConverter productsConverterImpl;
    private final ProductSpecification productSpecification;

    /**
     * Constructor that initializes the {@link ProductServiceImpl} with necessary repositories, converters, and specifications.
     *
     * @param repository            the repository for {@link ProductEntity} used by the generic service
     * @param converter             the converter used to convert between {@link ProductEntity} and {@link Product}
     * @param productRepository     the repository for {@link ProductEntity} used for product persistence
     * @param productsConverterImpl the converter used to convert a list of DTOs to entities
     * @param productSpecification  the specification used for dynamic product filtering
     */
    public ProductServiceImpl(
            CrudRepository<ProductEntity> repository,
            Converter<ProductEntity, Product> converter,
            ProductRepository productRepository,
            ProductsConverter productsConverterImpl,
            ProductSpecification productSpecification) {
        super(repository, converter);
        this.productRepository = productRepository;
        this.productsConverterImpl = productsConverterImpl;
        this.productSpecification = productSpecification;
    }

    /**
     * Adds a single product based on the provided {@link ProductDto}.
     * <p>
     * This method checks if a product with the same name and category already exists in the repository.
     * If the product exists, it throws a {@link ResourceAlreadyExistException}.
     * If the product does not exist, it saves the product entity and returns the generated ID.
     * </p>
     *
     * @param productDto the DTO containing information about the product to be added
     * @return the ID of the created product
     * @throws ResourceAlreadyExistException if a product with the same name and category already exists
     */
    public Long addProducts(ProductDto productDto) {
        if (productRepository.findByNameAndCategory(productDto.name(), productDto.category()).isPresent()) {
            throw new ResourceAlreadyExistException("Product already exists");
        }

        return productRepository.save(productDto.toProductEntity()).getId();
    }

    /**
     * Adds multiple products based on the provided list of {@link ProductDto}.
     * <p>
     * This method checks each product in the list to ensure that no product with the same name and category
     * already exists. If any product already exists, it throws a {@link ResourceAlreadyExistException}.
     * After validation, it saves all products and returns the list of generated product IDs.
     * </p>
     *
     * @param productsDto the list of DTOs containing information about the products to be added
     * @return a list of IDs of the created products
     * @throws ResourceAlreadyExistException if any product with the same name and category already exists
     */
    public List<Long> addProducts(List<ProductDto> productsDto) {

        productsDto.forEach(productDto -> {
            if (productRepository.findByNameAndCategory(
                    productDto.name(), productDto.category()).isPresent()) {
                throw new ResourceAlreadyExistException("Product: %s already exists".formatted(productDto));
            }
        });

        return productRepository.saveAll(productsConverterImpl.toProductEntities(productsDto))
                .stream()
                .map(ProductEntity::getId)
                .toList();
    }

    /**
     * Retrieves a list of all product categories.
     *
     * @return a list of all categories available in the product repository
     */
    @Override
    public List<String> getCategories() {
        return productRepository.getAllCategories();
    }

    /**
     * Retrieves a list of products based on the specified category.
     * <p>
     * This method checks if the provided category is valid (not null or empty). If valid, it fetches
     * the products belonging to that category and returns them as a list of {@link Product} objects.
     * </p>
     *
     * @param category the category of products to be retrieved
     * @return a list of products that belong to the specified category
     * @throws IllegalArgumentException if the category is null or empty
     */
    @Override
    public List<Product> getProductsByCategory(String category) {

        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        if (category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }

        return productsConverterImpl
                .toProduct(productRepository.findByCategory(category));
    }

    /**
     * Filters products based on the specified {@link ProductsFilterDto}.
     * <p>
     * This method utilizes a dynamic filtering mechanism via {@link ProductSpecification}. It applies
     * the provided filter criteria and returns a list of products matching those criteria.
     * </p>
     *
     * @param productsFilterDto the DTO containing the filter criteria for the products
     * @return a list of products that match the filter criteria
     */
    @Override
    public List<Product> filterProducts(ProductsFilterDto productsFilterDto) {

        return productRepository
                .findAll(productSpecification
                        .dynamicFilter(productsFilterDto.toProductFilterSpecification()))
                .stream()
                .map(ProductEntity::toProduct)
                .toList();
    }
}
