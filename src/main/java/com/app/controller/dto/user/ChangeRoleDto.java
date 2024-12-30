package com.app.controller.dto.user;

import com.app.model.Role;

/**
 * A DTO representing the data required to change a user's role.
 * <p>
 * This class contains the user ID and the new role that should be assigned to the user.
 * </p>
 */
public record ChangeRoleDto(
        /**
         * The unique identifier of the user whose role is to be changed.
         */
        Long userId,

        /**
         * The new role to be assigned to the user. This is an instance of the {@link Role} enum.
         */
        Role newRole) {
}
