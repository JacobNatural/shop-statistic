package com.app.service;

import com.app.controller.dto.user.*;

/**
 * Interface for managing user-related operations such as user creation, activation, password management, and role changes.
 * Provides methods for managing user accounts, including authentication, email updates, and password resets.
 */
public interface UserService {

    /**
     * Creates a new user based on the provided user data.
     *
     * @param createUserDto the DTO containing the data for the new user
     * @return the ID of the newly created user
     */
    Long createUser(CreateUserDto createUserDto);

    /**
     * Initializes the system by setting up initial configurations or data.
     * This method is typically called during application startup.
     */
    void init();

    /**
     * Activates a user account using the provided token.
     *
     * @param userTokenActivationDto the DTO containing the token for activation
     * @return the ID of the activated user
     */
    Long activateUser(UserTokenActivationDto userTokenActivationDto);

    /**
     * Refreshes a user's activation token based on the provided email.
     *
     * @param userEmailRefreshToken the DTO containing the user's email for token refresh
     * @return the ID of the user associated with the refreshed token
     */
    Long refreshToken(UserEmailRefreshToken userEmailRefreshToken);

    /**
     * Changes the password of an existing user.
     *
     * @param changePasswordDto the DTO containing the current and new password data
     * @param token             the token for user verification
     * @return the ID of the user whose password was changed
     */
    Long changePassword(ChangePasswordDto changePasswordDto, String token);

    /**
     * Changes the role of a user.
     *
     * @param changeRoleDto the DTO containing the user ID and the new role to be assigned
     * @return the ID of the user whose role was changed
     */
    Long changeRole(ChangeRoleDto changeRoleDto);

    /**
     * Handles a lost password scenario by sending a reset link to the user's email.
     *
     * @param emailDto the DTO containing the email address for the user who has lost their password
     * @return the ID of the user requesting the password reset
     */
    Long lostPassword(EmailDto emailDto);

    /**
     * Sets a new password for a user who has requested a reset.
     *
     * @param newPasswordDto the DTO containing the new password data
     * @return the ID of the user who updated their password
     */
    Long newPassword(NewPasswordDto newPasswordDto);

    /**
     * Changes the email address of a user.
     *
     * @param emailDto the DTO containing the new email address
     * @param token    the token for user verification
     * @return the ID of the user whose email was updated
     */
    Long changeEmail(EmailDto emailDto, String token);
}
