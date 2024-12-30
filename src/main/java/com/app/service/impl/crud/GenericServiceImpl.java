package com.app.service.impl.crud;

import com.app.converter.single.Converter;
import com.app.persistence.repository.CrudRepository;
import com.app.service.CrudService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * A generic implementation of the {@link CrudService} interface providing basic CRUD operations
 * for entities of type {@link T} and model type {@link U}.
 * <p>
 * This class provides common functionality for retrieving, deleting, and converting entities from the repository.
 * Subclasses can extend this class to customize or extend the behavior for specific entity types.
 * </p>
 *
 * @param <T> the entity type, which corresponds to the persistence model (e.g., entity)
 * @param <U> the model type, which corresponds to the service or DTO model (e.g., business model)
 */
@RequiredArgsConstructor
public abstract class GenericServiceImpl<T, U> implements CrudService<U> {

    /**
     * The repository used for interacting with the data source (e.g., database).
     */
    protected final CrudRepository<T> repository;

    /**
     * The converter used to convert between entity {@link T} and model {@link U}.
     */
    protected final Converter<T, U> converter;

    /**
     * Finds an entity by its ID and converts it to the model type.
     * <p>
     * If the entity is not found, an {@link EntityNotFoundException} is thrown.
     * </p>
     *
     * @param id the ID of the entity to find
     * @return the converted model {@link U}
     * @throws EntityNotFoundException if the entity with the specified ID is not found
     */
    @Override
    public U findById(Long id) {
        return converter.toModel(repository
                .findById(id).orElseThrow(()
                        -> new EntityNotFoundException("Element not found")));
    }

    /**
     * Finds entities by their IDs and converts them to the model type.
     * <p>
     * If some entities are not found, an {@link EntityNotFoundException} is thrown.
     * </p>
     *
     * @param ids the list of IDs of entities to find
     * @return a list of converted models {@link U}
     * @throws EntityNotFoundException if not all entities are found
     */
    @Override
    public List<U> findAllByIds(List<Long> ids) {
        var elements = repository.findAllById(ids)
                .stream()
                .map(converter::toModel)
                .toList();

        if (elements.size() == ids.size()) {
            return elements;
        }
        throw new EntityNotFoundException("Not all elements were found.");
    }

    /**
     * Removes an entity by its ID.
     * <p>
     * If the entity is found, it is deleted from the repository, and its ID is returned.
     * </p>
     *
     * @param id the ID of the entity to remove
     * @return the ID of the removed entity
     * @throws EntityNotFoundException if the entity with the specified ID is not found
     */
    @Override
    public Long removeElement(Long id) {
        var element = findById(id);
        repository.delete(converter.toEntity(element));
        return id;
    }

    /**
     * Removes multiple entities by their IDs.
     * <p>
     * Finds the entities corresponding to the provided IDs, deletes them from the repository, and returns the list of IDs.
     * If any entity is not found, an {@link EntityNotFoundException} is thrown.
     * </p>
     *
     * @param ids the list of IDs of the entities to remove
     * @return a list of IDs of the removed entities
     * @throws EntityNotFoundException if not all entities are found
     */
    @Override
    public List<Long> removeAllByIds(List<Long> ids) {
        var elements = findAllByIds(ids);

        repository.deleteAll(elements.stream().map(converter::toEntity).toList());
        return ids;
    }
}
