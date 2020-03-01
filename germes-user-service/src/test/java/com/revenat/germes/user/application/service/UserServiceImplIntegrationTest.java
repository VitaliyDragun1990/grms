package com.revenat.germes.user.application.service;

import com.revenat.germes.user.domain.model.User;
import com.revenat.germes.user.config.UserServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;

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

    private static final String JOE_12345 = "joe12345";

    private static final String ANNA_54321 = "anna54321";

    private static final String SECRET = "secret";

    @Autowired
    private UserService service;

    @Test
    void shouldNotFindAnyUserIfNoUserWasSaved() {
        final List<User> users = service.findAll();

        assertThat(users, hasSize(0));
    }

    @Test
    void shouldSaveUser() {
        final User user = new User();
        user.setUserName(JOE_12345);
        user.setPassword(SECRET);

        service.save(user);

        assertPresentUsers(user);
    }

    @Test
    void shouldFindAllUsers() {
        final User joe = new User();
        joe.setUserName(JOE_12345);
        joe.setPassword(SECRET);
        final User anna = new User();
        anna.setUserName(ANNA_54321);
        anna.setPassword(SECRET);
        service.save(joe);
        service.save(anna);

        final List<User> allUsers = service.findAll();

        assertThat(allUsers, hasSize(2));
        assertThat(allUsers, hasItem(equalTo(joe)));
        assertThat(allUsers, hasItem(equalTo(anna)));
    }

    @Test
    void shouldReturnEmptyOptionalIfNoUserWithSpecifiedIdWasFound() {
        final Optional<User> userOptional = service.findById(1);

        assertTrue(userOptional.isEmpty());
    }

    @Test
    void shouldFindUserById() {
        final User user = new User();
        user.setUserName(JOE_12345);
        user.setPassword(SECRET);
        service.save(user);

        final Optional<User> userOptional = service.findById(user.getId());

        assertTrue(userOptional.isPresent());
        assertThat(userOptional.get(), equalTo(user));
    }

    @Test
    void shouldDeleteUserById() {
        final User user = new User();
        user.setUserName(JOE_12345);
        user.setPassword(SECRET);
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
        final Optional<User> userOptional = service.findByUserName(JOE_12345);

        assertTrue(userOptional.isEmpty());
    }

    @Test
    void shouldFindUserByUsername() {
        final User user = new User();
        user.setUserName(JOE_12345);
        user.setPassword(SECRET);
        service.save(user);

        final Optional<User> userOptional = service.findByUserName(JOE_12345);

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
}