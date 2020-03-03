package com.revenat.germes.user.application.security;

import com.revenat.germes.common.core.shared.helper.Asserts;
import com.revenat.germes.common.core.shared.encrypter.Encrypter;
import com.revenat.germes.user.domain.model.User;
import com.revenat.germes.user.domain.model.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Vitaliy Dragun
 */
@Component
@RequiredArgsConstructor
class DBAuthenticator implements Authenticator {

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
