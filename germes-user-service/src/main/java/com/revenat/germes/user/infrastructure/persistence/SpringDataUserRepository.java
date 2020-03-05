package com.revenat.germes.user.infrastructure.persistence;

import com.revenat.germes.user.core.domain.model.User;
import com.revenat.germes.user.core.domain.model.UserRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Vitaliy Dragun
 */
@RepositoryDefinition(domainClass = User.class, idClass = UUID.class)
@Repository
public interface SpringDataUserRepository extends UserRepository {
}
