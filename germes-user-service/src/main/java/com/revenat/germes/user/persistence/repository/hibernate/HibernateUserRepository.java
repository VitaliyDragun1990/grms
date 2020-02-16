package com.revenat.germes.user.persistence.repository.hibernate;

import com.revenat.germes.infrastructure.cdi.qualifier.DBSource;
import com.revenat.germes.infrastructure.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.repository.hibernate.BaseHibernateRepository;
import com.revenat.germes.user.model.entity.User;
import com.revenat.germes.user.persistence.repository.UserRepository;
import org.hibernate.query.Query;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

/**
 * @author Vitaliy Dragun
 */
@Named
@DBSource
@Dependent
public class HibernateUserRepository extends BaseHibernateRepository implements UserRepository {

    @Inject
    public HibernateUserRepository(final SessionFactoryBuilder builder) {
        super(builder);
    }

    @Override
    public void save(final User user) {
        execute(session -> session.saveOrUpdate(user));
    }

    @Override
    public Optional<User> findById(final int userId) {
        return query(session -> Optional.ofNullable(session.get(User.class, userId)));
    }

    @Override
    public Optional<User> findByUserName(final String userName) {
        return query(session -> {
            final Query<User> query = session.createNamedQuery(User.QUERY_FIND_BY_USERNAME, User.class);
            query.setParameter("userName", userName);
            return query.uniqueResultOptional();
        });
    }

    @Override
    public void delete(final int userId) {
        execute(session -> {
            final User user = session.get(User.class, userId);
            if (user != null) {
                session.delete(user);
            }
        });
    }

    @Override
    public List<User> findAll() {
        return query(session -> session.createNamedQuery(User.QUERY_FIND_ALL, User.class).list());
    }
}
