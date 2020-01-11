package com.revenat.germes.persistence.hibernate;

import com.revenat.germes.application.infrastructure.exception.PersistenceException;
import com.revenat.germes.application.model.entity.geography.Address;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Coordinate;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.entity.person.Account;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import javax.annotation.PreDestroy;
import java.io.InputStream;
import java.util.Properties;

import static java.util.Objects.requireNonNull;


/**
 * Component responsible for managing
 * Hibernate session factory
 *
 * @author Vitaliy Dragun
 */
public class SessionFactoryBuilder {

    private final SessionFactory sessionFactory;

    public SessionFactoryBuilder() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(loadProperties()).build();

        final MetadataSources sources = new MetadataSources(registry);

        sources.addAnnotatedClass(City.class);
        sources.addAnnotatedClass(Account.class);
        sources.addAnnotatedClass(Station.class);
        sources.addAnnotatedClass(Coordinate.class);
        sources.addAnnotatedClass(Address.class);

        sessionFactory = sources.buildMetadata().buildSessionFactory();
    }

    private Properties loadProperties() {
        try {
            final Properties properties = new Properties();
            try (final InputStream in = SessionFactoryBuilder.class.getClassLoader().getResourceAsStream("application.properties")) {
                properties.load(requireNonNull(in, "Can not get input stream"));
                return properties;
            }
        } catch (final Exception e) {
            throw new PersistenceException("Error while loading application.properties:", e);
        }
    }

    /**
     * Return single instance of session factory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @PreDestroy
    void destroy() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
