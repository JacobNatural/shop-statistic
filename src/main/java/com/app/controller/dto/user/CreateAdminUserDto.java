package com.app.controller.dto.user;

import com.app.model.Role;
import com.app.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * A DTO representing the data required to create an admin user.
 * <p>
 * This class contains the necessary information for creating an admin user, such as username, name, surname, password, and email.
 * The values for these fields are injected from the application properties file.
 * </p>
 */
@Component
public record CreateAdminUserDto(
        /**
         * The username of the admin user, injected from the application properties.
         */
        @Value("${admin.username}")
        String username,

        /**
         * The first name of the admin user, injected from the application properties.
         */
        @Value("${admin.name}")
        String name,

        /**
         * The surname of the admin user, injected from the application properties.
         */
        @Value("${admin.surname}")
        String surname,

        /**
         * The password of the admin user, injected from the application properties.
         */
        @Value("${admin.password}")
        String password,

        /**
         * The email address of the admin user, injected from the application properties.
         */
        @Value("${admin.email}")
        String email) {

    /**
     * Converts this {@link CreateAdminUserDto} into a {@link UserEntity}.
     * <p>
     * This method creates a new {@link UserEntity} with the provided values for username, name, surname, password, and email,
     * and assigns it the role of {@link Role#ROLE_ADMIN}.
     * </p>
     *
     * @return a new {@link UserEntity} representing the admin user.
     */
    public UserEntity toUserEntity() {
        return UserEntity
                .builder()
                .username(username)
                .name(name)
                .surname(surname)
                .password(password)
                .email(email)
                .enable(true)
                .role(Role.ROLE_ADMIN)
                .build();
    }
}
