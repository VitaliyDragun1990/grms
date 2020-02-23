package com.revenat.germes.user.persistence.repository.spring;

import com.revenat.germes.user.model.entity.User;
import com.revenat.germes.user.persistence.repository.UserRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Dragun
 */
@RepositoryDefinition(domainClass = User.class, idClass = Integer.class)
@Repository
public interface SpringDataUserRepository extends UserRepository {
}
