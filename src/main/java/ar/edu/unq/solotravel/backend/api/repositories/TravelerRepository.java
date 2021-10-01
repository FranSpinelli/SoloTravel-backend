package ar.edu.unq.solotravel.backend.api.repositories;

import ar.edu.unq.solotravel.backend.api.models.Traveler;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface TravelerRepository extends UserBaseRepository<Traveler> {
    Optional<Traveler> findByGoogleId(String googleId);
}
