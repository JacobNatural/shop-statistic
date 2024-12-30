package com.app.controller;

import com.app.controller.dto.ResponseDto;
import com.app.exception.InvalidCredentialsException;
import com.app.exception.ResourceAlreadyExistException;
import com.app.exception.UserAlreadyActivatedException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for managing exceptions thrown across the application.
 * <p>
 * This class is responsible for catching various exceptions and returning the appropriate HTTP response
 * with a detailed error message. It uses {@link ResponseDto} to structure the response in a consistent way.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link IllegalArgumentException} and returns a 400 Bad Request status.
     * <p>
     * This method returns a {@link ResponseDto} containing the error message of the exception.
     * </p>
     *
     * @param e          the exception thrown.
     * @param webRequest the request that caused the exception.
     * @return a {@link ResponseDto} containing the exception message.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<String> illegalArgumentException(IllegalArgumentException e, WebRequest webRequest) {
        return new ResponseDto<>(e.getMessage());
    }

    /**
     * Handles {@link InvalidCredentialsException} and returns a 401 Unauthorized status.
     * <p>
     * This method returns a {@link ResponseDto} containing the error message of the exception.
     * </p>
     *
     * @param e          the exception thrown.
     * @param webRequest the request that caused the exception.
     * @return a {@link ResponseDto} containing the exception message.
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseDto<String> passwordNotMatch(Exception e, WebRequest webRequest) {
        return new ResponseDto<>(e.getMessage());
    }

    /**
     * Handles {@link EntityNotFoundException} and returns a 404 Not Found status.
     * <p>
     * This method returns a {@link ResponseDto} containing the error message of the exception.
     * </p>
     *
     * @param e          the exception thrown.
     * @param webRequest the request that caused the exception.
     * @return a {@link ResponseDto} containing the exception message.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDto<String> entityNotFound(Exception e, WebRequest webRequest) {
        return new ResponseDto<>(e.getMessage());
    }

    /**
     * Handles {@link UserAlreadyActivatedException} and {@link ResourceAlreadyExistException}
     * and returns a 409 Conflict status.
     * <p>
     * This method returns a {@link ResponseDto} containing the error message of the exception.
     * </p>
     *
     * @param e          the exception thrown.
     * @param webRequest the request that caused the exception.
     * @return a {@link ResponseDto} containing the exception message.
     */
    @ExceptionHandler({UserAlreadyActivatedException.class, ResourceAlreadyExistException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDto<String> userAlreadyActivated(Exception e, WebRequest webRequest) {
        return new ResponseDto<>(e.getMessage());
    }

    /**
     * Handles any unexpected {@link Exception} and returns a 500 Internal Server Error status.
     * <p>
     * This method catches all unhandled exceptions and returns a {@link ResponseDto} with the exception message.
     * </p>
     *
     * @param e          the exception thrown.
     * @param webRequest the request that caused the exception.
     * @return a {@link ResponseDto} containing the exception message.
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<String> handleUnexpectedExceptions(Exception e, WebRequest webRequest) {
        return new ResponseDto<>(e.getMessage());
    }
}
