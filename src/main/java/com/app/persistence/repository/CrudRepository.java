package com.app.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base repository interface for all entities with a primary key of type {@link Long}.
 * <p>
 * This interface extends {@link JpaRepository} and provides basic CRUD (Create, Read, Update, Delete) operations for the entity type {@code T}.
 * The entities managed by this repository must have a primary key of type {@link Long}.
 * </p>
 *
 * <p>
 * The interface is annotated with {@link NoRepositoryBean}, which indicates that Spring Data JPA should not create an instance of this repository interface directly.
 * Instead, other repository interfaces should extend this interface to inherit its methods.
 * </p>
 *
 * @param <T> the type of the entity to be handled by this repository
 * @see JpaRepository
 */
@NoRepositoryBean
public interface CrudRepository<T> extends JpaRepository<T, Long> {
    // No additional methods are needed, as basic CRUD operations are inherited from JpaRepository
}
