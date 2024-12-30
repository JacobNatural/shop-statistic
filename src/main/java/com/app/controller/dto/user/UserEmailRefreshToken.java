package com.app.controller.dto.user;

/**
 * A DTO representing the data required to refresh a user's email-related token.
 * <p>
 * This class contains the email address of the user for whom the token is to be refreshed.
 * </p>
 */
public record UserEmailRefreshToken(
        /**
         * The email address of the user whose token needs to be refreshed.
         */
        String email) {
}
