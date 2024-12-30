package com.app.security.dto;

/**
 * Data Transfer Object (DTO) used to represent both the access token and the refresh token.
 * <p>
 * This class encapsulates the two tokens used for user authentication and authorization:
 * <ul>
 *   <li><strong>accessToken</strong> - A token used for accessing protected resources.</li>
 *   <li><strong>refreshToken</strong> - A token used to refresh the access token when it expires.</li>
 * </ul>
 *
 *
 * @param accessToken  the access token that grants access to protected resources
 * @param refreshToken the refresh token used to obtain a new access token
 */
public record TokensDto(String accessToken, String refreshToken) {
}
