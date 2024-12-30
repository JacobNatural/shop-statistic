package com.app.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

/**
 * JPA Entity representing a verification token for user activation.
 * <p>
 * This class is a JPA entity mapped to the `verification_token` table in the database.
 * It stores the verification token used for user account activation and includes a reference
 * to the associated user. The entity also contains a method to validate the token by checking
 * if it has expired or not based on the stored timestamp.
 * </p>
 *
 * <p>
 * The entity is linked to the `users` table via a one-to-one relationship with the `UserEntity`.
 * The `timeStamp` represents the expiration time of the token in nanoseconds.
 * </p>
 */
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
@Entity
@Table(name = "verification_token")
public class VerificationTokenEntity extends BaseEntity {

    /**
     * The verification token.
     * <p>
     * This field stores the token used for user account activation.
     * </p>
     */
    private String token;

    /**
     * The timestamp representing the expiration time of the token.
     * <p>
     * This field stores the expiration timestamp of the verification token in nanoseconds.
     * If the current time is greater than this value, the token is considered expired.
     * </p>
     */
    private Long timeStamp;

    /**
     * The associated user for this verification token.
     * <p>
     * This field represents a one-to-one relationship with the `UserEntity` class, where the
     * token is associated with a specific user who is attempting to activate their account.
     * </p>
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", updatable = false)
    private UserEntity user;

    /**
     * Validates the token by checking its expiration time.
     * <p>
     * This method checks if the verification token has expired. It returns an {@link Optional}
     * containing the associated user if the token is still valid (i.e., its timestamp has not expired),
     * or an empty {@link Optional} if the token is expired.
     * </p>
     *
     * @return An {@link Optional} containing the user if the token is valid, or an empty {@link Optional} if expired.
     */
    public Optional<UserEntity> validate() {
        return Optional.ofNullable(timeStamp > System.nanoTime() ? user : null);
    }
}
