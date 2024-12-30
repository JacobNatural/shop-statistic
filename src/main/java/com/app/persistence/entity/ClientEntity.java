package com.app.persistence.entity;

import com.app.model.Client;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a client in the database.
 * <p>
 * This class is a JPA entity mapped to the `clients` table in the database. It contains
 * information about a client, including their name, surname, age, cash balance, and the
 * associated orders placed by the client. The class also provides a method to convert
 * the entity into a {@link Client} model object.
 * </p>
 *
 * <p>
 * The entity is mapped to the `clients` table, with a unique constraint ensuring that
 * the combination of name and surname is unique for each client.
 * </p>
 */
@ToString(exclude = "orders", callSuper = true)
@EqualsAndHashCode(callSuper = true, exclude = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "clients",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "surname"})
        }
)
public class ClientEntity extends BaseEntity {

    /**
     * The first name of the client.
     * <p>
     * This field stores the name of the client and is part of the unique constraint
     * along with the surname.
     * </p>
     */
    private String name;

    /**
     * The surname of the client.
     * <p>
     * This field stores the surname of the client and is part of the unique constraint
     * along with the name.
     * </p>
     */
    private String surname;

    /**
     * The age of the client.
     * <p>
     * This field stores the client's age, which is an integer value.
     * </p>
     */
    @Column(name = "age")
    private int age;

    /**
     * The cash balance of the client.
     * <p>
     * This field stores the amount of cash available for the client. It uses
     * {@link BigDecimal} to represent monetary values with high precision.
     * </p>
     */
    private BigDecimal cash;

    /**
     * The list of orders associated with the client.
     * <p>
     * This field defines a one-to-many relationship with the {@link OrderEntity}.
     * Each client can have multiple orders. The cascade operation of `REMOVE` ensures
     * that if a client is deleted, all associated orders are also removed.
     * </p>
     */
    @OneToMany(mappedBy = "clientEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    List<OrderEntity> orders = new ArrayList<>();

    /**
     * Converts the `ClientEntity` to a {@link Client} model object.
     * <p>
     * This method creates a new {@link Client} instance using the data from the
     * current `ClientEntity` object.
     * </p>
     *
     * @return A new instance of {@link Client} with the entity's data.
     */
    public Client toClient() {
        return new Client(id, name, surname, age, cash);
    }
}
