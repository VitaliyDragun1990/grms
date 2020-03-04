package com.revenat.germes.geography.infrastructure.persistence;

import com.revenat.germes.common.core.domain.model.EntityLoader;
import com.revenat.germes.common.core.shared.environment.StandardPropertyEnvironment;
import com.revenat.germes.common.core.shared.environment.source.ClassPathFilePropertySource;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("session entity loader")
class SessionEntityLoaderTest {

    private SessionFactoryBuilder sessionFactoryBuilder;

    private EntityLoader entityLoader;

    @BeforeEach
    void setUp() {
        sessionFactoryBuilder = new SessionFactoryBuilder(
                new StandardPropertyEnvironment(
                        new ClassPathFilePropertySource("application.properties")));
        entityLoader = new SessionEntityLoader(sessionFactoryBuilder);
    }

    @AfterEach
    void tearDown() {
        sessionFactoryBuilder.destroy();
    }

    @Test
    void shouldReturnEmptyOptionalIfNoEntityWithSpecifiedIdentifier() {
        final Optional<Person> result = entityLoader.load(Person.class, 999);

        assertTrue(result.isEmpty(), "should return empty optional if no entity with given id");
    }

    @Test
    void shouldReturnExistingEntityByIdentifier() {
        final Person person = saveNewPerson();

        final Optional<Person> result = entityLoader.load(Person.class, person.getId());

        assertTrue(result.isPresent(),  "should return optional with existing entity");
        assertThat(result.get(), equalTo(person));
    }

    private Person saveNewPerson() {
        final Person person = new Person("John Smith");

        saveEntity(person);

        return person;
    }

    private void saveEntity(final Person person) {
        try (final Session session = sessionFactoryBuilder.getSessionFactory().openSession()) {
            final Transaction transaction = session.getTransaction();
            transaction.begin();

            session.saveOrUpdate(person);

            transaction.commit();
        }
    }
}