package com.app.controller.dto.user;

/**
 * A DTO representing the data required to activate a user account.
 * <p>
 * This class contains the ID of the user to be activated.
 * </p>
 */
public record UserActivationDto(
        /**
         * The unique identifier of the user to be activated.
         */
        Long id) {
}
