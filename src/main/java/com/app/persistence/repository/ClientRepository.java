package com.app.persistence.repository;

import com.app.persistence.entity.ClientEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing {@link ClientEntity} entities in the database.
 * <p>
 * This interface provides methods for performing CRUD (Create, Read, Update, Delete) operations on {@link ClientEntity} objects.
 * Additionally, it provides custom query methods to retrieve clients based on specific criteria, such as searching by name and surname.
 * </p>
 *
 * <p>The repository extends {@link CrudRepository}, which provides basic CRUD operations.
 * Custom queries or other advanced querying mechanisms can also be added to this interface.</p>
 *
 * @see ClientEntity
 * @see CrudRepository
 */
@Repository
public interface ClientRepository extends CrudRepository<ClientEntity> {

    /**
     * Retrieves a {@link ClientEntity} by the client's name and surname.
     *
     * @param name    the name of the client
     * @param surname the surname of the client
     * @return an {@link Optional} containing the {@link ClientEntity} if found, or empty if no client exists with the given name and surname
     */
    Optional<ClientEntity> findByNameAndSurname(String name, String surname);
}
