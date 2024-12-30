package com.app.exception;

/**
 * Custom exception class that represents an invalid credentials error.
 * <p>
 * This exception is thrown when user credentials are invalid, typically during authentication
 * or login processes. It extends {@link RuntimeException}, making it an unchecked exception.
 * </p>
 */
public class InvalidCredentialsException extends RuntimeException {

    /**
     * Constructs a new InvalidCredentialsException with the specified detail message.
     *
     * @param message the detail message which provides more information about the exception
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
