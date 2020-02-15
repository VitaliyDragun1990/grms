package com.revenat.germes.presentation.admin.bean;

import com.revenat.germes.application.infrastructure.helper.Encrypter;
import com.revenat.germes.application.model.entity.person.User;
import com.revenat.germes.application.service.UserService;
import com.revenat.germes.presentation.admin.config.startup.Eager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;

/**
 * CDI-managed bean responsible for creating guest user if it does not exists at application startup
 *
 * @author Vitaliy Dragun
 */
@Named
@ApplicationScoped
@Eager
public class GuestUserInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestUserInitializer.class);

    public static final String GUEST = "guest";

    private UserService userService;

    private Encrypter encrypter;

    @Inject
    public GuestUserInitializer(@Default final UserService userService) {
        this.userService = userService;
        encrypter = new Encrypter();
    }

    /**
     * For CDI container purpose
     */
    GuestUserInitializer() {
    }

    @PostConstruct
    void init() {
        LOGGER.info("UserInitializer has been loaded.");

        if (userService.findByUserName(GUEST).isEmpty()) {
            final User user = new User();
            user.setUserName(GUEST);
            user.setPassword(encrypter.encryptSHA(GUEST));
            user.setCreatedAt(LocalDateTime.now());

            userService.save(user);
            LOGGER.info("User with username:'guest' and password:'guest' has been created");
        }
    }
}
