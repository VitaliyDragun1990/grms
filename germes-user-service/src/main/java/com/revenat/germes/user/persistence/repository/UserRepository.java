package com.revenat.germes.user.persistence.repository;


import com.revenat.germes.user.model.entity.User;

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
    User save(User user);

    /**
     * Returns user with specified identifier boxed into {@link Optional}
     *
     * @param userId unique identifier to search user with
     * @return optional with found user, or empty one otherwise
     */
    Optional<User> findById(int userId);

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
     * @param userId unique identifier to delete user with
     */
    void deleteById(int userId);

    /**
     * Returns all users
     *
     * @return list containing all users currently present in the persistence storage
     */
    List<User> findAll();
}
