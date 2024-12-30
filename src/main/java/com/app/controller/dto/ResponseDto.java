package com.app.controller.dto;

/**
 * A generic DTO representing a response with either data or an error message.
 * <p>
 * This class can hold a response with a generic type for data or an error message, but not both at the same time.
 * It provides constructors to initialize the response with either data or an error.
 * </p>
 *
 *
 */
public record ResponseDto<T>(
        /**
         * The data to be included in the response, can be of any type.
         */
        T data,

        /**
         * The error message to be included in the response, if any.
         */
        String error) {

    /**
     * Constructor to create a response with data and no error message.
     * <p>
     * Initializes the {@code error} field as {@code null}.
     * </p>
     *
     * @param data the data to be included in the response.
     */
    public ResponseDto(T data) {
        this(data, null);
    }

    /**
     * Constructor to create a response with an error message and no data.
     * <p>
     * Initializes the {@code data} field as {@code null}.
     * </p>
     *
     * @param error the error message to be included in the response.
     */
    public ResponseDto(String error) {
        this(null, error);
    }
}
