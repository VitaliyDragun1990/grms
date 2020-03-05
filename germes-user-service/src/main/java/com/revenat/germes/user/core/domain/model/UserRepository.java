package com.revenat.germes.user.core.domain.model;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Defines CRUD methods to access {@link User} objects in the persistence storage.
 *
 * @author Vitaliy Dragun
 */
public interface UserRepository {

    /**
     * Saves (creates or updates) specified user instance
     *
     * @param user user instance to save
     */
    User save(User user);

    /**
     * Returns user with specified identifier boxed into {@link Optional}
     *
     * @param id unique identifier to search user with
     * @return optional with found user, or empty one otherwise
     */
    Optional<User> findById(UUID id);

    /**
     * Returns user with specified username
     *
     * @param userName username to search user with
     * @return optional with found user, or empty one otherwise
     */
    Optional<User> findByUserName(String userName);

    /**
     * Deletes user with specified identifier
     *
     * @param id unique identifier to delete user with
     */
    void deleteById(UUID id);

    /**
     * Returns all users
     *
     * @return list containing all users currently present in the persistence storage
     */
    List<User> findAll();
}
