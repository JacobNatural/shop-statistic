package com.app.service.impl.crud;

import com.app.controller.dto.product.ProductDto;
import com.app.controller.dto.product.ProductsFilterDto;
import com.app.converter.many.ProductsConverter;
import com.app.converter.single.Converter;
import com.app.exception.ResourceAlreadyExistException;
import com.app.model.Product;
import com.app.persistence.entity.ProductEntity;
import com.app.persistence.repository.ProductRepository;
import com.app.persistence.repository.specification.ProductSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.app.data.ProductData.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private Converter<ProductEntity, Product> converter;

    @Mock
    private ProductsConverter productsConverter;

    @Mock
    private ProductSpecification productSpecification;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    @DisplayName("When adding a product and product already exist, throw an ResourceAlreadyExistException.")
    public void test1() {

        Mockito.when(repository.findByNameAndCategory(
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(PRODUCT_ENTITY_READ1));

        var productDto = new ProductDto(1L, "A", "C1", BigDecimal.ONE);

        Assertions.assertThatThrownBy(() -> service.addProducts(productDto))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessage("Product already exists");

        Mockito.verify(repository, Mockito.times(1))
                .findByNameAndCategory(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

    }

    @Test
    @DisplayName("When adding a product, return the product ID upon successful completion.")
    public void test2() {

        var inOrder = Mockito.inOrder(repository);

        Mockito.when(repository.findByNameAndCategory(
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(repository.save(ArgumentMatchers.any(ProductEntity.class)))
                .thenReturn(PRODUCT_ENTITY_READ1);

        var productDto = new ProductDto(1L, "A", "C1", BigDecimal.ONE);

        Assertions.assertThat(service.addProducts(productDto))
                .isEqualTo(1L);

        inOrder.verify(repository, Mockito.times(1))
                .findByNameAndCategory(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        inOrder.verify(repository, Mockito.times(1))
                .save(ArgumentMatchers.any(ProductEntity.class));
    }

    @Test
    @DisplayName("When adding products and one of the products already exists, throw an ResourceAlreadyExistException.")
    public void test3() {

        Mockito.when(repository.findByNameAndCategory("A", "C1"))
                .thenReturn(Optional.empty());

        Mockito.when(repository.findByNameAndCategory("AA", "C2"))
                .thenReturn(Optional.of(PRODUCT_ENTITY_READ1));

        var productDto1 = new ProductDto(1L, "A", "C1", BigDecimal.ONE);
        var productDto2 = new ProductDto(2L, "AA", "C2", BigDecimal.TWO);

        Assertions.assertThatThrownBy(() ->
                        service.addProducts(List.of(productDto1, productDto2)))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessage("Product: %s already exists".formatted(productDto2));

        Mockito.verify(repository, Mockito.times(2))
                .findByNameAndCategory(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("When adding products, return the products ID upon successful completion.")
    public void test4() {

        var inOrder = Mockito.inOrder(repository, productsConverter);

        Mockito.when(repository.findByNameAndCategory(
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(repository.saveAll(ArgumentMatchers.anyList()))
                .thenReturn(List.of(PRODUCT_ENTITY_READ1, PRODUCT_ENTITY_READ2));

        Mockito.when(productsConverter.toProductEntities(ArgumentMatchers.anyList()))
                .thenReturn(List.of(PRODUCT_ENTITY_READ1, PRODUCT_ENTITY_READ2));

        var productDto1 = new ProductDto(1L, "A", "C1", BigDecimal.ONE);
        var productDto2 = new ProductDto(2L, "AA", "C2", BigDecimal.TWO);

        Assertions.assertThat(service.addProducts(List.of(productDto1, productDto2)))
                .isEqualTo(List.of(1L, 2L));

        inOrder.verify(repository, Mockito.times(2))
                .findByNameAndCategory(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        inOrder.verify(productsConverter, Mockito.times(1))
                .toProductEntities(ArgumentMatchers.anyList());

        inOrder.verify(repository, Mockito.times(1))
                .saveAll(ArgumentMatchers.anyList());
    }

    @Test
    @DisplayName("When getting all available categories, return a list of categories.")
    public void test5(){

        Mockito.when(repository.getAllCategories())
                .thenReturn(List.of("C1", "C2", "C3"));

        Assertions.assertThat(service.getCategories())
                .isEqualTo(List.of("C1", "C2", "C3"));

        Mockito.verify(repository, Mockito.times(1))
                .getAllCategories();
    }

    @Test
    @DisplayName("When getting all available categories, return a list of categories.")
    public void test6(){

        Mockito.when(repository.getAllCategories())
                .thenReturn(List.of("C1", "C2", "C3"));

        Assertions.assertThat(service.getCategories())
                .isEqualTo(List.of("C1", "C2", "C3"));

        Mockito.verify(repository, Mockito.times(1))
                .getAllCategories();
    }

    @Test
    @DisplayName("When getting a product by category, and the category is null, throw an IllegalArgumentException.")
    public void test7(){

        Assertions.assertThatThrownBy(() -> service.getProductsByCategory(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Category cannot be null");
    }

    @Test
    @DisplayName("When getting a product by category and the category is empty, throw an IllegalArgumentException.")
    public void test8(){

        Assertions.assertThatThrownBy(() -> service.getProductsByCategory(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Category cannot be empty");
    }

    @Test
    @DisplayName("When getting a product by category and the repository return an empty list, return an empty list.")
    public void test9() {

        Mockito.when(repository.findByCategory(ArgumentMatchers.anyString()))
                .thenReturn(List.of());

        Assertions.assertThat(service.getProductsByCategory("home"))
                .isEqualTo(List.of());

        Mockito.verify(repository, Mockito.times(1))
                .findByCategory(ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("When getting a product by category and the repository return a list of products entity, " +
            "return a list of products.")
    public void test10() {

        var inOrder = Mockito.inOrder(repository, productsConverter);

        Mockito.when(repository.findByCategory(ArgumentMatchers.anyString()))
                .thenReturn(List.of(PRODUCT_ENTITY_READ1, PRODUCT_ENTITY_READ2 ));

        Mockito.when(productsConverter.toProduct(ArgumentMatchers.anyList()))
                        .thenReturn(List.of(PRODUCT1, PRODUCT2));

        Assertions.assertThat(service.getProductsByCategory("groceries"))
                .isEqualTo(List.of(PRODUCT1, PRODUCT2));

        inOrder.verify(repository, Mockito.times(1))
                .findByCategory(ArgumentMatchers.anyString());

        inOrder.verify(productsConverter, Mockito.times(1))
                .toProduct(ArgumentMatchers.anyList());
    }

    @Test
    @DisplayName("When getting products with a dynamic filter, the repository returns an empty list," +
            " and the service returns an empty list as well.")
    public void test11(){

        Mockito.when(repository.findAll(ArgumentMatchers.any(Specification.class)))
                .thenReturn(List.of());

        Mockito.when(productSpecification.dynamicFilter(ArgumentMatchers.any()))
                .thenReturn(((root, query, criteriaBuilder) -> criteriaBuilder.conjunction()));

        var productFilterDto = new ProductsFilterDto(BigDecimal.valueOf(10), null, null);

        Assertions.assertThat(service.filterProducts(productFilterDto))
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When getting products with a dynamic filter, the repository returns a list of product entities," +
            " and the service returns a list of products.")
    public void test12(){

        Mockito.when(repository.findAll(ArgumentMatchers.any(Specification.class)))
                .thenReturn(List.of(PRODUCT_ENTITY_READ1,PRODUCT_ENTITY_READ2 ));

        Mockito.when(productSpecification.dynamicFilter(ArgumentMatchers.any()))
                .thenReturn(((root, query, criteriaBuilder) -> criteriaBuilder.conjunction()));

        var productFilterDto = new ProductsFilterDto(BigDecimal.valueOf(10), null, null);

        Assertions.assertThat(service.filterProducts(productFilterDto))
                .isEqualTo(List.of(PRODUCT1, PRODUCT2));
    }
}
