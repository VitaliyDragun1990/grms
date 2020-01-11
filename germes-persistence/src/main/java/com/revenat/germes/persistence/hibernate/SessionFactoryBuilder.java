package com.revenat.germes.persistence.hibernate;

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


/**
 * Component responsible for managing
 * Hibernate session factory
 *
 * @author Vitaliy Dragun
 */
public class SessionFactoryBuilder {

    private final SessionFactory sessionFactory;

    public SessionFactoryBuilder() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().build();

        final MetadataSources sources = new MetadataSources(registry);

        sources.addAnnotatedClass(City.class);
        sources.addAnnotatedClass(Station.class);
        sources.addAnnotatedClass(Coordinate.class);
        sources.addAnnotatedClass(Address.class);
        sources.addAnnotatedClass(Account.class);

        sessionFactory = sources.buildMetadata().buildSessionFactory();
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
