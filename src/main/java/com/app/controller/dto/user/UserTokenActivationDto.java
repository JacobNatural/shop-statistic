package com.app.controller.dto.user;

/**
 * A DTO representing the data required to activate a user account using a token.
 * <p>
 * This class contains the user ID and the activation token.
 * </p>
 */
public record UserTokenActivationDto(
        /**
         * The unique identifier of the user whose account is to be activated.
         */
        Long userId,

        /**
         * The activation token used to verify and activate the user's account.
         */
        String token) {
}
