package com.revenat.germes.user.database;

import com.revenat.germes.user.domain.model.User;
import com.revenat.germes.user.domain.model.UserRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Dragun
 */
@RepositoryDefinition(domainClass = User.class, idClass = Integer.class)
@Repository
public interface SpringDataUserRepository extends UserRepository {
}
