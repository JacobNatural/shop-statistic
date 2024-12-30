package com.app.controller.dto.user;

/**
 * A DTO representing the data required to reset a user's password.
 * <p>
 * This class contains the new password, the confirmation of the new password, and a token for validation.
 * </p>
 */
public record NewPasswordDto(
        /**
         * The new password that the user wants to set.
         */
        String newPassword,

        /**
         * The confirmation of the new password, which should match the new password.
         */
        String confirmPassword,

        /**
         * A token used for validating the password reset request.
         */
        String token) {
}
