package com.revenat.germes.user.core.application;


import com.revenat.germes.user.core.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Provides API for the {@link User} management
 *
 * @author Vitaliy Dragun
 */
public interface UserService {

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
    Optional<User> findById(UUID userId);

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
    void delete(UUID userId);

    /**
     * Returns all users
     *
     * @return list containing all users currently present in the persistence storage
     */
    List<User> findAll();
}
