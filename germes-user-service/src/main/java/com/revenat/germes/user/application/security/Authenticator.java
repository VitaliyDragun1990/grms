package com.revenat.germes.user.application.security;

import com.revenat.germes.user.domain.model.User;

import java.util.Optional;

/**
 * Abstraction over authentication process
 *
 * @author Vitaliy Dragun
 */
public interface Authenticator {

    /**
     * Checks if provided credentials are valid and returns found user
     *
     * @param userName       username of the user
     * @param hashedPassword hashed password of the user
     * @return optional with found user for specified credentials, or empty optional otherwise
     */
    Optional<User> authenticate(String userName, String hashedPassword);
}
