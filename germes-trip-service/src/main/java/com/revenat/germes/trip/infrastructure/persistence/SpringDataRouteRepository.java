package com.revenat.germes.trip.infrastructure.persistence;

import com.revenat.germes.trip.core.domain.model.Route;
import com.revenat.germes.trip.core.domain.model.RouteRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Dragun
 */
@RepositoryDefinition(domainClass = Route.class, idClass = Integer.class)
@Repository
public interface SpringDataRouteRepository extends RouteRepository {
}
