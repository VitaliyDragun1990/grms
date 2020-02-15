package com.revenat.germes.persistence.infrastructure.hibernate;

import com.revenat.germes.application.infrastructure.environment.Environment;
import com.revenat.germes.persistence.infrastructure.hibernate.interceptor.TimestampInterceptor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.reflections.Reflections;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Entity;
import java.util.Set;


/**
 * Component responsible for managing
 * Hibernate session factory
 *
 * @author Vitaliy Dragun
 */
@Named
@ApplicationScoped
public class SessionFactoryBuilder {

    private static final String PROPERTIES_PREFIX = "hibernate";

    private SessionFactory sessionFactory;

    @Inject
    public SessionFactoryBuilder(final Environment environment) {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(environment.getProperties(PROPERTIES_PREFIX))
                .build();

        final MetadataSources sources = new MetadataSources(registry);

        findEntityClasses().forEach(sources::addAnnotatedClass);

        final Metadata metadata = sources.getMetadataBuilder().build();
        sessionFactory = metadata.getSessionFactoryBuilder()
                .applyInterceptor(new TimestampInterceptor())
                .build();
    }

    /**
     * Only for CDI container to be able to create proxy of this application scoped bean
     */
    SessionFactoryBuilder() {

    }

    private Set<Class<?>> findEntityClasses() {
        final Reflections reflections = new Reflections("com.revenat.germes.application.model.entity");
        return reflections.getTypesAnnotatedWith(Entity.class);
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
