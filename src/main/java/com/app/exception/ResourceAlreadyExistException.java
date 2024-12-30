package com.app.exception;

/**
 * Custom exception class that represents a scenario where a resource already exists.
 * <p>
 * This exception is thrown when an attempt is made to create or add a resource that already exists
 * in the system, typically during operations like adding a new user, product, or other entities.
 * It extends {@link RuntimeException}, making it an unchecked exception.
 * </p>
 */
public class ResourceAlreadyExistException extends RuntimeException {

    /**
     * Constructs a new ResourceAlreadyExistException with the specified detail message.
     *
     * @param message the detail message which provides more information about the exception
     */
    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
