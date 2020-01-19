package com.revenat.germes.application.service.impl;

import com.revenat.germes.application.infrastructure.exception.flow.InvalidParameterException;
import com.revenat.germes.application.infrastructure.exception.flow.ValidationException;
import com.revenat.germes.application.model.entity.person.User;
import com.revenat.germes.application.service.UserService;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.repository.hibernate.HibernateUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("user service")
class UserServiceImplIntegrationTest {

    private static final String JOE_12345 = "joe12345";

    private static final String ANNA_54321 = "anna54321";

    private static final String SECRET = "secret";

    private UserService service;

    private SessionFactoryBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new SessionFactoryBuilder();
        service = new UserServiceImpl(new HibernateUserRepository(builder));
    }

    @AfterEach
    void tearDown() {
        builder.destroy();
    }

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
    void shouldFailToSaveUserIfUsernameIsNull() {
        User user = new User();
        user.setUserName(null);
        user.setPassword(SECRET);

        final ValidationException e = assertThrows(ValidationException.class, () -> service.save(user));

        assertValidation(e, "userName", User.class, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    void shouldFailToSaveUserIfPasswordIsNull() {
        User user = new User();
        user.setUserName(JOE_12345);
        user.setPassword(null);

        final ValidationException e = assertThrows(ValidationException.class, () -> service.save(user));

        assertValidation(e, "password", User.class, "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    void shouldFailToSaveUserIfUsernameIsToShort() {
        User user = new User();
        user.setUserName("aa");
        user.setPassword(SECRET);

        final ValidationException e = assertThrows(ValidationException.class, () -> service.save(user));

        assertValidation(e, "userName", User.class, "{javax.validation.constraints.Size.message}");
    }

    @Test
    void shouldFailToSaveUserIfUsernameIsToLong() {
        User user = new User();
        user.setUserName("a".repeat(25));
        user.setPassword(SECRET);

        final ValidationException e = assertThrows(ValidationException.class, () -> service.save(user));

        assertValidation(e, "userName", User.class, "{javax.validation.constraints.Size.message}");
    }

    @Test
    void shouldFailToSaveUserIfPasswordIsToShort() {
        User user = new User();
        user.setUserName(JOE_12345);
        user.setPassword("ss");

        final ValidationException e = assertThrows(ValidationException.class, () -> service.save(user));

        assertValidation(e, "password", User.class, "{javax.validation.constraints.Size.message}");
    }

    @Test
    void shouldFailToSaveUserIfPasswordIsToLong() {
        User user = new User();
        user.setUserName(JOE_12345);
        user.setPassword("s".repeat(25));

        final ValidationException e = assertThrows(ValidationException.class, () -> service.save(user));

        assertValidation(e, "password", User.class, "{javax.validation.constraints.Size.message}");
    }

    @Test
    void shouldFailToSaveNotInitializeUser() {

        assertThrows(InvalidParameterException.class, () -> service.save(null));
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
        assertThrows(InvalidParameterException.class, () -> service.findByUserName(null));
    }

    private void assertValidation(final ValidationException ex, final String fieldName,
                                  final Class<?> clz, final String messageKey) {
        assertThat(ex.getConstraints(), hasSize(greaterThan(0)));
        final ConstraintViolation<?> constraint = ex.getConstraints().iterator().next();
        assertThat(constraint.getMessageTemplate(), equalTo(messageKey));
        assertThat(constraint.getPropertyPath().toString(), equalTo(fieldName));
        assertThat(constraint.getRootBeanClass(), equalTo(clz));
    }

    private void assertPresentUsers(final User... users) {
        final List<User> allUsers = service.findAll();

        assertThat(allUsers, hasSize(users.length));
        for (final User user : users) {
            assertThat(allUsers, hasItem(equalTo(user)));
        }
    }
}