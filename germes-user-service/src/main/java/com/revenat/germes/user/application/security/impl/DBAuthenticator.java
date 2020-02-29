package com.revenat.germes.user.application.security.impl;

import com.revenat.germes.infrastructure.helper.Asserts;
import com.revenat.germes.infrastructure.helper.encrypter.Encrypter;
import com.revenat.germes.user.application.security.Authenticator;
import com.revenat.germes.user.model.entity.User;
import com.revenat.germes.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * @author Vitaliy Dragun
 */
@RequiredArgsConstructor
public class DBAuthenticator implements Authenticator {

    private final UserRepository userRepository;

    private final Encrypter encrypter;

    @Override
    public Optional<User> authenticate(final String userName, final String hashedPassword) {
        Asserts.assertNotNullOrBlank(userName, "userName can not be null or blank");
        Asserts.assertNotNullOrBlank(hashedPassword, "hashedPassword can not be null or blank");

        return userRepository.findByUserName(userName)
                .filter(user -> hashedPassword.equals(encrypter.encryptSHA(user.getPassword())));
    }
}
