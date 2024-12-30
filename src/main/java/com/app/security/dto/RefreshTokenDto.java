package com.app.security.dto;

/**
 * Data Transfer Object (DTO) used to represent a refresh token.
 * <p>
 * This class encapsulates a refresh token that is returned after a successful authentication
 * to allow clients to obtain a new access token without requiring the user to log in again.
 * </p>
 *
 * @param token the refresh token used to request a new access token
 */
public record RefreshTokenDto(String token) {
}
