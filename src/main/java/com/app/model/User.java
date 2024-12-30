package com.app.model;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Represents a user within the application. A user has personal details and account information.
 * <p>
 * This class holds the attributes for a user, including their ID, username, personal information,
 * password, email, account status (enabled or disabled), and assigned role in the system.
 * </p>
 */
@ToString(callSuper = true)
@RequiredArgsConstructor
public class User {

    /**
     * The unique identifier of the user.
     */
    private final Long id;

    /**
     * The username of the user, typically used for login and authentication.
     */
    private final String username;

    /**
     * The first name of the user.
     */
    private final String name;

    /**
     * The surname (last name) of the user.
     */
    private final String surname;

    /**
     * The password of the user, which should be stored securely (e.g., hashed and salted).
     */
    private final String password;

    /**
     * The email address of the user, used for notifications and communication.
     */
    private final String email;

    /**
     * A flag indicating whether the user's account is enabled (active) or disabled (inactive).
     * A disabled account may not be able to log in or access certain resources.
     */
    private final Boolean enable;

    /**
     * The role assigned to the user. Roles define the permissions and access control for the user.
     * The role could be something like a worker, leader, or admin.
     */
    private final Role role;

}
