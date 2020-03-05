package com.revenat.germes.user.core.application;

import com.revenat.germes.user.config.UserServiceTestConfig;
import com.revenat.germes.user.core.domain.model.Role;
import com.revenat.germes.user.core.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vitaliy Dragun
 */
@SpringJUnitConfig({UserServiceTestConfig.class})
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("user service")
class UserServiceImplIntegrationTest {

    private static final String JOE_SMITH = "joe_smith";

    private static final String ANNA_SWANSON = "anna_swanson";

    private static final String PASSWORD = "secret";

    private static final String REGISTRATION_IP = "localhost";

    @Autowired
    private UserService service;

    @Test
    void shouldNotFindAnyUserIfNoUserWasSaved() {
        final List<User> users = service.findAll();

        assertThat(users, hasSize(0));
    }

    @Test
    void shouldSaveUser() {
        final User user = buildOrdinalUser(JOE_SMITH);

        service.save(user);

        assertPresentUsers(user);
    }

    @Test
    void shouldFindAllUsers() {
        final User joe = buildOrdinalUser(JOE_SMITH);
        final User anna = buildOrdinalUser(ANNA_SWANSON);
        service.save(joe);
        service.save(anna);

        final List<User> allUsers = service.findAll();

        assertThat(allUsers, hasSize(2));
        assertThat(allUsers, hasItem(equalTo(joe)));
        assertThat(allUsers, hasItem(equalTo(anna)));
    }

    @Test
    void shouldReturnEmptyOptionalIfNoUserWithSpecifiedIdWasFound() {
        final Optional<User> userOptional = service.findById(UUID.randomUUID());

        assertTrue(userOptional.isEmpty());
    }

    @Test
    void shouldFindUserById() {
        final User user = buildOrdinalUser(JOE_SMITH);
        service.save(user);

        final Optional<User> userOptional = service.findById(user.getId());

        assertTrue(userOptional.isPresent());
        assertThat(userOptional.get(), equalTo(user));
    }

    @Test
    void shouldDeleteUserById() {
        final User user = buildOrdinalUser(JOE_SMITH);
        service.save(user);

        service.delete(user.getId());

        final Optional<User> userOptional = service.findById(user.getId());
        assertTrue(userOptional.isEmpty());
    }

    @Test
    void shouldFailToSaveNotInitializeUser() {

    assertThrows(NullPointerException.class, () -> service.save(null));
    }

    @Test
    void shouldReturnEmptyOptionalIfNoUserWithSpecifiedUsername() {
        final Optional<User> userOptional = service.findByUserName(JOE_SMITH);

        assertTrue(userOptional.isEmpty());
    }

    @Test
    void shouldFindUserByUsername() {
        final User user = buildOrdinalUser(JOE_SMITH);
        service.save(user);

        final Optional<User> userOptional = service.findByUserName(JOE_SMITH);

        assertTrue(userOptional.isPresent());
        assertThat(userOptional.get(), equalTo(user));
    }

    @Test
    void shouldFailToFindUserByUsernameIfSpecifiedUsernameIsNull() {
        assertThrows(NullPointerException.class, () -> service.findByUserName(null));
    }

    private void assertPresentUsers(final User... users) {
        final List<User> allUsers = service.findAll();

        assertThat(allUsers, hasSize(users.length));
        for (final User user : users) {
            assertThat(allUsers, hasItem(equalTo(user)));
        }
    }

    private User buildOrdinalUser(final String username) {
        final String[] data = username.split("_");
        final String firstName = capitalize(data[0]);
        final String lastName = capitalize(data[1]);

        final User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserName(username);
        user.setPassword(PASSWORD);
        user.setCreatedAt(LocalDateTime.now());
        user.setRegistrationIp(REGISTRATION_IP);
        user.setRole(Role.USER);

        return user;
    }

    private String capitalize(final String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}