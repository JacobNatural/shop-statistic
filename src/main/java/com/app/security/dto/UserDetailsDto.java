package com.app.security.dto;

/**
 * Data Transfer Object (DTO) used to represent the details of a user.
 * <p>
 * This class encapsulates the information related to a user's authentication and authorization:
 * <ul>
 *   <li><strong>username</strong> - The unique username of the user.</li>
 *   <li><strong>password</strong> - The password of the user (in its encoded form).</li>
 *   <li><strong>enable</strong> - A boolean indicating if the user account is enabled or not.</li>
 *   <li><strong>role</strong> - The role assigned to the user (e.g., "USER", "ADMIN", etc.).</li>
 * </ul>
 *
 *
 * @param username the unique username of the user
 * @param password the password (encoded) of the user
 * @param enable   indicates if the user's account is enabled
 * @param role     the role of the user, used for authorization
 */
public record UserDetailsDto(String username, String password, boolean enable, String role) {
}
