package ar.edu.unq.solotravel.backend.api.repositories;

import ar.edu.unq.solotravel.backend.api.models.TravelAgency;

import java.util.Optional;

public interface TravelAgencyRepository extends UserBaseRepository<TravelAgency>{
    Optional<TravelAgency> findByTripsId(Integer tripId);
}
