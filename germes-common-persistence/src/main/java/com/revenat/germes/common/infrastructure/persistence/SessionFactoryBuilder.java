package com.revenat.germes.common.infrastructure.persistence;

import com.revenat.germes.common.core.shared.environment.Environment;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;


/**
 * Component responsible for managing
 * Hibernate session factory
 *
 * @author Vitaliy Dragun
 */
@Named
@ApplicationScoped
public class SessionFactoryBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionFactoryBuilder.class);

    private static final String PROPERTIES_PREFIX = "hibernate";

    private SessionFactory sessionFactory;

    @Inject
    public SessionFactoryBuilder(final Environment environment) {

        final ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(environment.getProperties(PROPERTIES_PREFIX))
                .build();

        final MetadataSources sources = new MetadataSources(registry);

        findEntityClasses(environment).forEach(sources::addAnnotatedClass);

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

    private Collection<Class<?>> findEntityClasses(final Environment env) {
        final String basePackage = env.getProperty("hibernate.base.package", "com.revenat.germes");
        LOGGER.debug("base package to scan for entities: {}", basePackage);

        try (ScanResult scanResult =
                     new ClassGraph()
                             .enableAllInfo()
                             .whitelistPackages(basePackage)
                             .scan()) {
            final ClassInfoList classesWithAnnotation = scanResult.getClassesWithAnnotation("javax.persistence.Entity");
            return classesWithAnnotation.loadClasses();
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
