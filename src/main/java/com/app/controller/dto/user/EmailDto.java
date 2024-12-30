package com.app.controller.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * A DTO representing an email address.
 * <p>
 * This class contains a single email field and enforces validation constraints on the email format and nullability.
 * </p>
 */
public record EmailDto(
        /**
         * The email address of the user.
         * <p>
         * This field must be non-null and follow a valid email format.
         * </p>
         */
        @NotNull(message = "Email cannot be null")
        @Email(message = "Email is invalid")
        String email) {
}
