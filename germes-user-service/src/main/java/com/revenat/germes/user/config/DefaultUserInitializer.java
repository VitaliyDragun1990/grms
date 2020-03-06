package com.revenat.germes.user.config;

import com.revenat.germes.user.core.domain.model.Role;
import com.revenat.germes.user.core.domain.model.User;
import com.revenat.germes.user.core.domain.model.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author Vitaliy Dragun
 */
@Component
@AllArgsConstructor
class DefaultUserInitializer {

    private static final String PREFIX = "init.data.users.";

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUserInitializer.class);

    @Autowired
    private final Environment env;

    private final UserRepository userRepository;

    @EventListener(ContextRefreshedEvent.class)
    void initializeDefaultUsers() {
        initializeUser(Role.ADMIN);
        initializeUser(Role.USER);
    }

    private void initializeUser(final Role role) {
        final String roleName = role.name().toLowerCase();
        final String userName = env.getRequiredProperty(PREFIX + roleName + ".username");


        if (userRepository.findByUserName(userName).isEmpty()) {
            final String password = env.getProperty(PREFIX + roleName + ".password");
            final String firstName = env.getProperty(PREFIX + roleName + ".first-name");
            final String lastName = env.getProperty(PREFIX + roleName + ".last-name");

            final User user = new User();
            user.setUserName(userName);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setRole(role);
            userRepository.save(user);

            LOGGER.debug("Default user created: role={}, userName={}, password={}", roleName, userName, password);
        }
    }
}
