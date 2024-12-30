package com.app.security.dto;

/**
 * Data Transfer Object (DTO) used to represent an authentication error.
 * <p>
 * This class encapsulates an error message that is returned when an authentication error occurs,
 * such as an invalid username or password.
 * </p>
 *
 * @param error the error message describing the authentication failure
 */
public record AuthenticationErrorDto(String error) {
}
