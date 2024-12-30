package com.app.security.dto;

/**
 * Data Transfer Object (DTO) used for authentication purposes.
 * <p>
 * This class is used to encapsulate the user's authentication data, including their username and password.
 * It is typically used when the user submits login credentials for authentication.
 * </p>
 *
 * @param username the username of the user attempting to authenticate
 * @param password the password of the user attempting to authenticate
 */
public record AuthenticationDto(String username, String password) {
}
