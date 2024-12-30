package com.app.converter.single.impl;

import com.app.converter.single.Converter;
import com.app.model.Product;
import com.app.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link Converter} interface for converting between {@link ProductEntity}
 * and {@link Product} objects.
 * <p>
 * This class provides the conversion logic for transforming a {@link ProductEntity} (representing a
 * database entity) to a {@link Product} (a business model) and vice versa.
 * </p>
 */
@Component
public class ProductConverterImpl implements Converter<ProductEntity, Product> {

    /**
     * Converts a {@link ProductEntity} to a {@link Product} model object.
     *
     * @param productEntity the {@link ProductEntity} object to convert
     * @return the corresponding {@link Product} model
     */
    @Override
    public Product toModel(ProductEntity productEntity) {
        return productEntity.toProduct();
    }

    /**
     * Converts a {@link Product} model object to a {@link ProductEntity}.
     *
     * @param product the {@link Product} model to convert
     * @return the corresponding {@link ProductEntity} object
     */
    @Override
    public ProductEntity toEntity(Product product) {
        return product.toProductEntity();
    }
}
