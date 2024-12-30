package com.app.controller.dto.order;

import java.util.List;

/**
 * A generic DTO that represents a grouping of elements by a specific key.
 * <p>
 * This class is used to group a list of elements of type {@link U} under a common key of type {@link T}.
 * </p>
 *
 * @param <T> The type of the key by which the elements are grouped.
 * @param <U> The type of the elements in the group.
 */
public record GroupByDto<T, U>(
        /**
         * The key by which the elements are grouped.
         */
        T key,

        /**
         * The list of elements of type {@link U} that are grouped under the key.
         */
        List<U> elements) {
}
