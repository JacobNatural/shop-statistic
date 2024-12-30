package com.app.controller.dto.user;

/**
 * A DTO representing the data required to change a user's password.
 * <p>
 * This class contains the current password, the new password, and the confirmation of the new password.
 * </p>
 */
public record ChangePasswordDto(
        /**
         * The current password of the user.
         */
        String currentPassword,

        /**
         * The new password the user wants to set.
         */
        String newPassword,

        /**
         * The confirmation of the new password, which should match the new password.
         */
        String newPasswordConfirmation) {
}
