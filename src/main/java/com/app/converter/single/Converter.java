package com.app.converter.single;

/**
 * A generic interface for converting between two types: an entity and a model.
 * <p>
 * This interface defines methods to convert an object of type {@code T} to an object of type {@code U}
 * (e.g., from an entity to a model), and vice versa (from a model to an entity).
 * </p>
 *
 * @param <T> the source type (entity)
 * @param <U> the target type (model)
 */
public interface Converter<T, U> {

    /**
     * Converts an object of type {@code T} (entity) to an object of type {@code U} (model).
     *
     * @param t the object of type {@code T} to convert
     * @return the converted object of type {@code U}
     */
    U toModel(T t);

    /**
     * Converts an object of type {@code U} (model) to an object of type {@code T} (entity).
     *
     * @param u the object of type {@code U} to convert
     * @return the converted object of type {@code T}
     */
    T toEntity(U u);
}
