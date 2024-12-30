package com.app.persistence.entity;

import com.app.model.Product;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a product in the database.
 * <p>
 * This class is a JPA entity mapped to the `products` table in the database. It represents
 * a product that can be ordered by clients. The class provides a method to convert the
 * entity into a {@link Product} model object.
 * </p>
 *
 * <p>
 * The entity is mapped to the `products` table, and each product has a unique combination
 * of name and category. It is also associated with a list of orders that reference this
 * product.
 * </p>
 */
@ToString(exclude = "orders")
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "category"})
        }
)
public class ProductEntity extends BaseEntity {

    /**
     * The name of the product.
     * <p>
     * This field stores the name of the product, which must be unique within a given category.
     * </p>
     */
    private String name;

    /**
     * The category of the product.
     * <p>
     * This field stores the category under which the product falls. The combination of
     * `name` and `category` must be unique in the database.
     * </p>
     */
    private String category;

    /**
     * The price of the product.
     * <p>
     * This field stores the price of the product. The price is represented using a
     * {@link BigDecimal} to ensure accurate representation of decimal values.
     * </p>
     */
    private BigDecimal price;

    /**
     * The list of orders that contain this product.
     * <p>
     * This field establishes a one-to-many relationship with the `OrderEntity` class.
     * Multiple orders can reference the same product. The `REMOVE` cascade ensures that
     * orders are removed if the associated product is deleted. The `orphanRemoval` ensures
     * that orders without products are removed.
     * </p>
     */
    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    List<OrderEntity> orders = new ArrayList<>();

    /**
     * Converts the `ProductEntity` to a {@link Product} model object.
     * <p>
     * This method creates a new {@link Product} instance using the data from the
     * current `ProductEntity` object.
     * </p>
     *
     * @return A new instance of {@link Product} with the entity's data.
     */
    public Product toProduct() {
        return new Product(id, name, category, price);
    }
}
