package com.revenat.germes.presentation.admin.bean;

import com.revenat.germes.application.infrastructure.helper.Encrypter;
import com.revenat.germes.application.model.entity.person.User;
import com.revenat.germes.application.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;

/**
 * Creates guest user if not exists at application startup
 *
 * @author Vitaliy Dragun
 */
@Named
@ApplicationScoped()
public class GuestUserInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestUserInitializer.class);

    public static final String GUEST = "guest";

    private final UserService userService;

    private final Encrypter encrypter;

    @Inject
    public GuestUserInitializer(final UserService userService) {
        this.userService = userService;
        encrypter = new Encrypter();
    }

    /**
     * Subscribes to lifecycle events of the bean and effectively told CDI container
     * to load it on startup (analog to @Startup EJB annotation)
     */
    void init(@Observes @Initialized(ApplicationScoped.class) final Object event) {
        LOGGER.info("UserInitializer has been load.");

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
