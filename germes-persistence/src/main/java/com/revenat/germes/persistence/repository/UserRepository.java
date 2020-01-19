package com.revenat.germes.persistence.repository;

import com.revenat.germes.application.model.entity.person.User;

import java.util.List;
import java.util.Optional;

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
    void save(User user);

    /**
     * Returns user with specified identifier boxed into {@link Optional}
     *
     * @param userId unique identifier to search user with
     * @return optional with found user, or empty one otherwise
     */
    Optional<User> findById(int userId);

    /**
     * Deletes user with specified identifier
     *
     * @param userId unique identifier to delete user with
     */
    void delete(int userId);

    /**
     * Returns all users
     *
     * @return list containing all users currently present in the persistence storage
     */
    List<User> findAll();
}
