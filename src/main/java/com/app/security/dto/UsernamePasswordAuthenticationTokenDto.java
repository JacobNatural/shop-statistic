package com.app.security.dto;

/**
 * Data Transfer Object (DTO) used to represent the username and role
 * of a user during authentication.
 * <p>
 * This DTO is commonly used to carry authentication information
 * after a successful login, typically containing the user's
 * username and the role they are assigned in the system.
 * </p>
 *
 * @param username the unique username of the user
 * @param role     the role assigned to the user (e.g., "USER", "ADMIN", etc.)
 */
public record UsernamePasswordAuthenticationTokenDto(String username, String role) {
}
