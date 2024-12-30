package com.app.service;

import java.util.List;

/**
 * A generic interface that provides CRUD (Create, Read, Update, Delete) operations for entities of type {@link U}.
 * This interface can be used to implement basic operations for managing entities in a service layer.
 *
 * @param <U> the type of the entity being managed
 */
public interface CrudService<U> {

    /**
     * Finds an entity by its ID.
     *
     * @param id the ID of the entity to be found
     * @return the entity with the specified ID, or {@code null} if not found
     */
    U findById(Long id);

    /**
     * Finds multiple entities by their IDs.
     *
     * @param ids the list of IDs of the entities to be found
     * @return a list of entities corresponding to the specified IDs
     */
    List<U> findAllByIds(List<Long> ids);

    /**
     * Removes an entity by its ID.
     *
     * @param id the ID of the entity to be removed
     * @return the ID of the removed entity
     */
    Long removeElement(Long id);

    /**
     * Removes multiple entities by their IDs.
     *
     * @param ids the list of IDs of the entities to be removed
     * @return a list of IDs of the removed entities
     */
    List<Long> removeAllByIds(List<Long> ids);
}
