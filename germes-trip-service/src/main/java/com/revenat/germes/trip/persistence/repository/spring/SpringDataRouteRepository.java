package com.revenat.germes.trip.persistence.repository.spring;

import com.revenat.germes.trip.model.entity.Route;
import com.revenat.germes.trip.persistence.repository.RouteRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Dragun
 */
@RepositoryDefinition(domainClass = Route.class, idClass = Integer.class)
@Repository
public interface SpringDataRouteRepository extends RouteRepository {
}
