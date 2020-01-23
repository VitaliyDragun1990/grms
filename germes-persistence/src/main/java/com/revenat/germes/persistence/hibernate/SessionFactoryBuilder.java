package com.revenat.germes.persistence.hibernate;

import com.revenat.germes.application.infrastructure.exception.PersistenceException;
import com.revenat.germes.persistence.hibernate.interceptor.TimestampInterceptor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.reflections.Reflections;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.Entity;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import static java.util.Objects.requireNonNull;


/**
 * Component responsible for managing
 * Hibernate session factory
 *
 * @author Vitaliy Dragun
 */
@Named
@ApplicationScoped
public class SessionFactoryBuilder {

    private final SessionFactory sessionFactory;

    public SessionFactoryBuilder() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(loadProperties()).build();

        final MetadataSources sources = new MetadataSources(registry);

        findEntityClasses().forEach(sources::addAnnotatedClass);

        final Metadata metadata = sources.getMetadataBuilder().build();
        sessionFactory = metadata.getSessionFactoryBuilder()
                .applyInterceptor(new TimestampInterceptor())
                .build();
    }

    private Set<Class<?>> findEntityClasses() {
        Reflections reflections = new Reflections("com.revenat.germes.application.model.entity");
        return reflections.getTypesAnnotatedWith(Entity.class);
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
    public void destroy() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
