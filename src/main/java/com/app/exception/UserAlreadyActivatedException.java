package com.app.exception;

/**
 * Custom exception class that represents a scenario where a user is already activated.
 * <p>
 * This exception is thrown when an attempt is made to activate a user who has already been activated.
 * It extends {@link RuntimeException}, making it an unchecked exception.
 * </p>
 */
public class UserAlreadyActivatedException extends RuntimeException {

    /**
     * Constructs a new UserAlreadyActivatedException with the specified detail message.
     *
     * @param message the detail message which provides more information about the exception
     */
    public UserAlreadyActivatedException(String message) {
        super(message);
    }
}
