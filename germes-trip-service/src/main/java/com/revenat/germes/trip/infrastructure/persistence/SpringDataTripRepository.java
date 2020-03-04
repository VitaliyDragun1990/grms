package com.revenat.germes.trip.infrastructure.persistence;

import com.revenat.germes.trip.core.domain.model.Trip;
import com.revenat.germes.trip.core.domain.model.TripRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Dragun
 */
@RepositoryDefinition(domainClass = Trip.class, idClass = Integer.class)
@Repository
public interface SpringDataTripRepository extends TripRepository {
}
