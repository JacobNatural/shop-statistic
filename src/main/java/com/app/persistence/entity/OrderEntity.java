package com.app.persistence.entity;

import com.app.model.Order;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing an order placed by a client in the database.
 * <p>
 * This class is a JPA entity mapped to the `orders` table in the database. It represents
 * an order, which includes references to the client placing the order and the product
 * being ordered. The class provides a method to convert the entity into a {@link Order}
 * model object.
 * </p>
 *
 * <p>
 * The entity is mapped to the `orders` table, with relationships to the `ClientEntity`
 * and `ProductEntity` classes, establishing a many-to-one relationship for both fields.
 * </p>
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    /**
     * The client who placed the order.
     * <p>
     * This field establishes a many-to-one relationship with the {@link ClientEntity}.
     * Each order is associated with one client. The `MERGE` cascade ensures that any
     * changes made to the client entity are synchronized with the order entity.
     * </p>
     */
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "client_id")
    private ClientEntity clientEntity;

    /**
     * The product being ordered.
     * <p>
     * This field establishes a many-to-one relationship with the {@link ProductEntity}.
     * Each order is associated with one product. The `MERGE` and `PERSIST` cascades
     * ensure that changes to the product or new product entities are propagated to
     * the order entity.
     * </p>
     */
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    /**
     * Converts the `OrderEntity` to a {@link Order} model object.
     * <p>
     * This method creates a new {@link Order} instance using the data from the
     * current `OrderEntity` object, including the associated client and product.
     * </p>
     *
     * @return A new instance of {@link Order} with the entity's data.
     */
    public Order toOrder() {
        return new Order(id, clientEntity.toClient(), productEntity.toProduct());
    }
}
