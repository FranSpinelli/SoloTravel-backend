package ar.edu.unq.solotravel.backend.api.repositories;

import ar.edu.unq.solotravel.backend.api.models.Trip;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends CrudRepository<Trip, Integer>, JpaSpecificationExecutor<Trip> {

    List<Trip> findAll(Specification<Trip> spec);
    Optional<Trip> findById(Integer id);
}
