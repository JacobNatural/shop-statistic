package com.app.persistence.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Base class for entities that require an ID field.
 * <p>
 * This class serves as a common superclass for all entity classes in the application
 * that need an automatically generated unique identifier. It provides the basic
 * functionality for persisting entities in the database with an {@link Id} annotated
 * field and a generated value strategy.
 * </p>
 * <p>
 * The `@MappedSuperclass` annotation indicates that this class is not itself an
 * entity but serves as a base for other entities. It defines common fields and logic
 * for entities in the application.
 * </p>
 */
@EqualsAndHashCode
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class BaseEntity {

    /**
     * The unique identifier for this entity.
     * <p>
     * This ID is automatically generated and managed by the persistence framework.
     * It is used to uniquely identify an instance of the entity in the database.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
}
