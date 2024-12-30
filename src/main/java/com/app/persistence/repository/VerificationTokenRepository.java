package com.app.persistence.repository;

import com.app.persistence.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link VerificationTokenEntity} entities in the persistence layer.
 * <p>
 * This interface extends {@link JpaRepository} to provide basic CRUD functionality for {@link VerificationTokenEntity} entities,
 * such as saving, updating, deleting, and retrieving verification token records from the database.
 * </p>
 */
public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, Long> {

    /**
     * Retrieves a verification token by its token value.
     * <p>
     * This method returns an {@link Optional} containing the {@link VerificationTokenEntity} associated with the given token.
     * If no token is found, an empty {@link Optional} is returned.
     * </p>
     *
     * @param token the token value of the verification token to retrieve
     * @return an {@link Optional} containing the {@link VerificationTokenEntity} if found, otherwise an empty {@link Optional}
     */
    Optional<VerificationTokenEntity> findByToken(String token);

    /**
     * Retrieves a verification token by the user ID associated with it.
     * <p>
     * This method returns an {@link Optional} containing the {@link VerificationTokenEntity} associated with the given user ID.
     * If no token is found for the specified user, an empty {@link Optional} is returned.
     * </p>
     *
     * @param userId the ID of the user whose verification token is to be retrieved
     * @return an {@link Optional} containing the {@link VerificationTokenEntity} if found, otherwise an empty {@link Optional}
     */
    Optional<VerificationTokenEntity> findByUserId(Long userId);
}
