package com.app.security.service.impl;

import com.app.persistence.repository.UserRepository;
import com.app.security.dto.RefreshTokenDto;
import com.app.security.dto.TokensDto;
import com.app.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

/**
 * Service implementation for handling JWT token creation, parsing, and refreshing.
 * <p>
 * This service is responsible for generating access and refresh tokens, validating existing tokens,
 * and refreshing tokens when necessary. It uses JWT (JSON Web Token) to create signed tokens for
 * user authentication and authorization. The service uses user details from the database to generate tokens.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final UserRepository userRepository;
    private final SecretKey secretKey;
    private final PasswordEncoder passwordEncoder;

    @Value("${tokens.refresh.expiration_time_ms}")
    private long refreshExpirationTimeMs;

    @Value("${tokens.access.expiration_time_ms}")
    private long accessExpirationTimeMs;

    @Value("${tokens.refresh.access_token_expiration_time_ms_property}")
    private String accessTokenExpirationTimeMsProperty;

    @Value("${tokens.prefix}")
    private String prefix;

    /**
     * Generates access and refresh tokens for the authenticated user.
     * <p>
     * This method creates an access token and a refresh token for the user based on the provided
     * authentication object. It uses the user's ID, the current time, and expiration times for the
     * tokens to generate JWT tokens.
     * </p>
     *
     * @param authentication the authentication object containing the user's credentials
     * @return the generated {@link TokensDto} containing the access and refresh tokens
     */
    @Override
    public TokensDto generateToken(Authentication authentication) {
        var userFromDb = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Authentication failed"));

        var userId = userFromDb.getId();
        var currentTime = new Date();
        var accessTokenExpirationTime = new Date(currentTime.getTime() + accessExpirationTimeMs);
        var refreshTokenExpirationTime = new Date(currentTime.getTime() + refreshExpirationTimeMs);
        return getTokens(userId, currentTime, accessTokenExpirationTime, refreshTokenExpirationTime);
    }

    /**
     * Parses the access token from the authorization header and retrieves the associated authentication.
     * <p>
     * This method extracts the token from the authorization header, verifies its validity, and retrieves
     * the user details associated with the token. It then creates and returns a {@link UsernamePasswordAuthenticationToken}.
     * </p>
     *
     * @param header the authorization header containing the access token
     * @return the {@link UsernamePasswordAuthenticationToken} representing the authenticated user
     * @throws IllegalArgumentException if the authorization header is incorrect or the token is invalid
     */
    @Override
    public UsernamePasswordAuthenticationToken parseAccessToken(String header) {
        if (!header.startsWith(prefix)) {
            throw new IllegalArgumentException("Authorization header is incorrect");
        }

        var token = header.replaceAll(prefix, "");
        expirationTime(token);

        var userId = id(token);

        return userRepository.findById(userId)
                .map(userFromDb -> {
                    var userDto = userFromDb.toUsernamePasswordAuthenticationTokenDto();
                    return new UsernamePasswordAuthenticationToken(
                            userDto.username(),
                            null,
                            List.of(new SimpleGrantedAuthority(userDto.role()))
                    );
                }).orElseThrow(() -> new IllegalArgumentException("Authorization failed"));
    }

    /**
     * Refreshes the access token using the provided refresh token.
     * <p>
     * This method validates the refresh token, checks if the associated access token is still valid,
     * and generates a new set of tokens if possible.
     * </p>
     *
     * @param refreshTokenDto the DTO containing the refresh token
     * @return the new {@link TokensDto} containing the new access and refresh tokens
     * @throws IllegalArgumentException if the refresh token is invalid or null
     * @throws IllegalStateException    if the old access token has expired
     */
    @Override
    public TokensDto refreshToken(RefreshTokenDto refreshTokenDto) {
        if (refreshTokenDto == null) {
            throw new IllegalArgumentException("Refresh token cannot be null");
        }

        var refreshToken = refreshTokenDto.token();
        expirationTime(refreshToken);

        var accessTokenExpirationTime = accessTokenExpirationTimeFromRefreshToken(refreshToken);
        if (accessTokenExpirationTime < System.currentTimeMillis()) {
            throw new IllegalStateException("Cannot create a new access token - old access token is expired");
        }

        var userId = id(refreshToken);
        var currentTime = new Date();
        var newAccessTokenExpirationTime = new Date(currentTime.getTime() + accessTokenExpirationTime);
        var refreshTokenExpirationTime = expirationTime(refreshToken);

        return getTokens(userId, currentTime, newAccessTokenExpirationTime, refreshTokenExpirationTime);
    }

    /**
     * Helper method to generate access and refresh tokens.
     *
     * @param userId                     the user ID associated with the tokens
     * @param currentTime                the current time of token creation
     * @param accessTokenExpirationTime  the expiration time of the access token
     * @param refreshTokenExpirationTime the expiration time of the refresh token
     * @return the generated {@link TokensDto} containing the access and refresh tokens
     */
    private TokensDto getTokens(Long userId, Date currentTime, Date accessTokenExpirationTime, Date refreshTokenExpirationTime) {
        var accessToken = Jwts
                .builder()
                .subject(userId + "")
                .expiration(accessTokenExpirationTime)
                .issuedAt(currentTime)
                .signWith(secretKey)
                .compact();

        var refreshToken = Jwts
                .builder()
                .subject(userId + "")
                .expiration(refreshTokenExpirationTime)
                .issuedAt(currentTime)
                .claim(accessTokenExpirationTimeMsProperty, accessTokenExpirationTime.getTime())
                .signWith(secretKey)
                .compact();

        return new TokensDto(accessToken, refreshToken);
    }

    /**
     * Helper method to parse claims from the token.
     *
     * @param token the JWT token
     * @return the claims parsed from the token
     */
    private Claims claims(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts the user ID from the token.
     *
     * @param token the JWT token
     * @return the user ID contained in the token
     */
    public Long id(String token) {
        return Long.parseLong(claims(token).getSubject());
    }

    /**
     * Extracts the expiration time from the token.
     *
     * @param token the JWT token
     * @return the expiration time of the token
     */
    private Date expirationTime(String token) {
        return claims(token).getExpiration();
    }

    /**
     * Extracts the access token expiration time from the refresh token.
     *
     * @param refreshToken the refresh token
     * @return the expiration time of the access token
     */
    private Long accessTokenExpirationTimeFromRefreshToken(String refreshToken) {
        return claims(refreshToken)
                .get(accessTokenExpirationTimeMsProperty, Long.class);
    }
}
