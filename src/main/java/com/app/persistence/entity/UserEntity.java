package com.app.persistence.entity;

import com.app.model.Role;
import com.app.security.dto.UserDetailsDto;
import com.app.security.dto.UsernamePasswordAuthenticationTokenDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * JPA Entity representing a user in the database.
 * <p>
 * This class is a JPA entity mapped to the `users` table in the database. It contains fields
 * for the user's username, name, surname, password, email, role, and enable status.
 * The class provides various methods to convert the entity to different data transfer objects
 * (DTOs) and to perform specific user-related actions such as updating role, email, or password.
 * </p>
 *
 * <p>
 * The entity is mapped to the `users` table, and each user has a unique username. The user's
 * role is stored as an enumerated type, and the `enable` field determines whether the user's
 * account is active or not.
 * </p>
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    /**
     * The username of the user.
     * <p>
     * This field stores the unique username of the user, which is used for authentication.
     * </p>
     */
    private String username;

    /**
     * The name of the user.
     * <p>
     * This field stores the first name of the user.
     * </p>
     */
    private String name;

    /**
     * The surname of the user.
     * <p>
     * This field stores the surname of the user.
     * </p>
     */
    private String surname;

    /**
     * The password of the user.
     * <p>
     * This field stores the encrypted password for the user.
     * </p>
     */
    private String password;

    /**
     * The email address of the user.
     * <p>
     * This field stores the email address associated with the user.
     * </p>
     */
    private String email;

    /**
     * The enable status of the user.
     * <p>
     * This field indicates whether the user account is active (`true`) or disabled (`false`).
     * </p>
     */
    private Boolean enable;

    /**
     * The role of the user.
     * <p>
     * This field stores the role of the user, which is an enumerated type {@link Role}.
     * The role determines the user's access rights within the system (e.g., worker, leader, admin).
     * </p>
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Creates a new `UserEntity` with an updated password.
     * <p>
     * This method returns a new `UserEntity` instance with the updated password while keeping
     * the other fields unchanged.
     * </p>
     *
     * @param password The new password to be set.
     * @return A new instance of `UserEntity` with the updated password.
     */
    public UserEntity withPassword(String password) {
        return UserEntity
                .builder()
                .id(id)
                .username(username)
                .name(name)
                .surname(surname)
                .password(password)
                .email(email)
                .enable(enable)
                .role(role)
                .build();
    }

    /**
     * Creates a new `UserEntity` with the account marked as enabled (activated).
     * <p>
     * This method returns a new `UserEntity` instance with the same data, but the `enable` field
     * is set to `true`, indicating the user account is activated.
     * </p>
     *
     * @return A new instance of `UserEntity` with the account activated.
     */
    public UserEntity toActivateUserEntity() {
        return UserEntity
                .builder()
                .id(id)
                .username(username)
                .name(name)
                .surname(surname)
                .password(password)
                .email(email)
                .enable(true)
                .role(role)
                .build();
    }

    /**
     * Creates a new `UserEntity` with a different role.
     * <p>
     * This method returns a new `UserEntity` instance with the same data, but with the specified
     * role instead of the current role.
     * </p>
     *
     * @param role The new role to be assigned to the user.
     * @return A new instance of `UserEntity` with the updated role.
     */
    public UserEntity toNewRoleEntity(Role role) {
        return UserEntity
                .builder()
                .id(id)
                .username(username)
                .name(name)
                .surname(surname)
                .password(password)
                .email(email)
                .enable(true)
                .role(role)
                .build();
    }

    /**
     * Creates a new `UserEntity` with a different email.
     * <p>
     * This method returns a new `UserEntity` instance with the same data, but with the specified
     * email instead of the current email.
     * </p>
     *
     * @param email The new email to be assigned to the user.
     * @return A new instance of `UserEntity` with the updated email.
     */
    public UserEntity toNewEmailEntity(String email) {
        return UserEntity
                .builder()
                .id(id)
                .username(username)
                .name(name)
                .surname(surname)
                .password(password)
                .email(email)
                .enable(true)
                .role(role)
                .build();
    }

    /**
     * Converts the `UserEntity` to a `UserDetailsDto`.
     * <p>
     * This method converts the `UserEntity` into a `UserDetailsDto`, which is a simplified data
     * transfer object that includes the username, password, enable status, and role of the user.
     * </p>
     *
     * @return A `UserDetailsDto` containing the user's details.
     */
    public UserDetailsDto toUserDetailsDto() {
        return new UserDetailsDto(username, password, enable, role.toString());
    }

    /**
     * Checks if the user is activated.
     * <p>
     * This method returns `true` if the user's account is enabled, indicating that the user is
     * activated. Otherwise, it returns `false`.
     * </p>
     *
     * @return `true` if the user is activated, `false` otherwise.
     */
    public boolean isActivate() {
        return enable;
    }

    /**
     * Converts the `UserEntity` to a `UsernamePasswordAuthenticationTokenDto`.
     * <p>
     * This method converts the `UserEntity` into a DTO used for authentication purposes,
     * including the username and role of the user.
     * </p>
     *
     * @return A `UsernamePasswordAuthenticationTokenDto` containing the user's username and role.
     */
    public UsernamePasswordAuthenticationTokenDto toUsernamePasswordAuthenticationTokenDto() {
        return new UsernamePasswordAuthenticationTokenDto(username, role.toString());
    }
}
