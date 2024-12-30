package com.app.security.service;

import com.app.security.dto.RefreshTokenDto;
import com.app.security.dto.TokensDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * Interface for handling JWT token generation, parsing, and refreshing operations.
 * <p>
 * This interface defines the necessary methods for working with JSON Web Tokens (JWT) in an authentication system.
 * Implementations of this interface are responsible for generating access and refresh tokens, parsing the access token
 * from an HTTP request, and refreshing the tokens based on the refresh token.
 * </p>
 */
public interface TokenService {

    /**
     * Generates a new access and refresh token for the given authenticated user.
     * <p>
     * This method creates an access token and a refresh token for a user based on the provided authentication object.
     * The generated tokens are signed with a secret key and contain expiration times.
     * </p>
     *
     * @param authentication the authentication object containing the authenticated user's details
     * @return a {@link TokensDto} object containing the generated access and refresh tokens
     */
    TokensDto generateToken(Authentication authentication);

    /**
     * Parses the access token from a provided token string and retrieves the associated authentication.
     * <p>
     * This method is responsible for extracting the JWT from the authorization header (or other source),
     * verifying its validity, and using it to create a {@link UsernamePasswordAuthenticationToken} that represents
     * the authenticated user.
     * </p>
     *
     * @param token the JWT token in the authorization header or request
     * @return a {@link UsernamePasswordAuthenticationToken} containing the authenticated user's details
     * @throws IllegalArgumentException if the token is invalid or cannot be parsed
     */
    UsernamePasswordAuthenticationToken parseAccessToken(String token);

    /**
     * Refreshes the access token using the provided refresh token.
     * <p>
     * This method validates the provided refresh token and uses it to generate a new set of tokens,
     * including a fresh access token and a new refresh token. The method ensures that the refresh token
     * is valid and has not expired before issuing the new tokens.
     * </p>
     *
     * @param refreshTokenDto a {@link RefreshTokenDto} containing the refresh token to be used for refreshing the access token
     * @return a {@link TokensDto} containing the newly generated access and refresh tokens
     * @throws IllegalArgumentException if the refresh token is invalid or null
     * @throws IllegalStateException    if the refresh token is expired or otherwise cannot be used to generate a new access token
     */
    TokensDto refreshToken(RefreshTokenDto refreshTokenDto);

    /**
     * Extracts the user ID from a JWT token.
     * <p>
     * This method is used to extract the user ID from the provided token's claims.
     * </p>
     *
     * @param token the JWT token containing the user ID as its subject
     * @return the user ID extracted from the token
     */
    Long id(String token);
}
