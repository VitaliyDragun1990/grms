package com.revenat.germes.user.application.security.impl;

import com.revenat.germes.infrastructure.helper.Asserts;
import com.revenat.germes.infrastructure.helper.encrypter.Encrypter;
import com.revenat.germes.user.application.security.Authenticator;
import com.revenat.germes.user.application.service.UserService;
import com.revenat.germes.user.model.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * @author Vitaliy Dragun
 */
@RequiredArgsConstructor
public class DBAuthenticator implements Authenticator {

    private final UserService userService;

    private final Encrypter encrypter;

    @Override
    public Optional<User> authenticate(final String userName, final String hashedPassword) {
        Asserts.assertNotNullOrBlank(userName, "userName can not be null or blank");
        Asserts.assertNotNullOrBlank(hashedPassword, "hashedPassword can not be null or blank");

        return userService.findByUserName(userName)
                .filter(user -> hashedPassword.equals(encrypter.encryptSHA(user.getPassword())));
    }
}
