package com.app.controller.dto.user;

import com.app.model.Role;
import com.app.persistence.entity.UserEntity;

/**
 * A DTO representing the data required to create a user.
 * <p>
 * This class contains the necessary information to register a new user, such as username, name, surname, password, password confirmation, and email.
 * </p>
 */
public record CreateUserDto(
        /**
         * The username of the user being created.
         */
        String username,

        /**
         * The first name of the user being created.
         */
        String name,

        /**
         * The surname of the user being created.
         */
        String surname,

        /**
         * The password for the user being created.
         */
        String password,

        /**
         * The confirmation of the password for the user being created. This should match the password.
         */
        String passwordConfirmation,

        /**
         * The email address of the user being created.
         */
        String email) {

    /**
     * Converts this {@link CreateUserDto} into a {@link UserEntity}.
     * <p>
     * This method creates a new {@link UserEntity} representing the user to be created, with the provided values for username, name, surname, password, and email.
     * The user is assigned the role of {@link Role#ROLE_WORKER} and is initially set as disabled.
     * </p>
     *
     * @return a new {@link UserEntity} representing the user to be created.
     */
    public UserEntity toCreateUserEntity() {
        return UserEntity
                .builder()
                .username(username)
                .name(name)
                .surname(surname)
                .password(password)
                .email(email)
                .enable(false)
                .role(Role.ROLE_WORKER)
                .build();
    }
}
