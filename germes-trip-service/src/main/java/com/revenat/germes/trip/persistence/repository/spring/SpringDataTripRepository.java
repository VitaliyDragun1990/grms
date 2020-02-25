package com.revenat.germes.trip.persistence.repository.spring;

import com.revenat.germes.trip.model.entity.Trip;
import com.revenat.germes.trip.persistence.repository.TripRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Dragun
 */
@RepositoryDefinition(domainClass = Trip.class, idClass = Integer.class)
@Repository
public interface SpringDataTripRepository extends TripRepository {
}
