package com.app.persistence.repository;

import com.app.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link UserEntity} entities in the persistence layer.
 * <p>
 * This interface extends {@link JpaRepository} to provide basic CRUD functionality for {@link UserEntity} entities,
 * such as saving, updating, deleting, and retrieving user records from the database.
 * </p>
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Retrieves a user by their username.
     * <p>
     * This method returns an {@link Optional} containing the {@link UserEntity} associated with the given username.
     * If no user is found, an empty {@link Optional} is returned.
     * </p>
     *
     * @param username the username of the user to retrieve
     * @return an {@link Optional} containing the {@link UserEntity} if found, otherwise an empty {@link Optional}
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Retrieves a user by their email address.
     * <p>
     * This method returns an {@link Optional} containing the {@link UserEntity} associated with the given email address.
     * If no user is found, an empty {@link Optional} is returned.
     * </p>
     *
     * @param email the email address of the user to retrieve
     * @return an {@link Optional} containing the {@link UserEntity} if found, otherwise an empty {@link Optional}
     */
    Optional<UserEntity> findByEmail(String email);
}
