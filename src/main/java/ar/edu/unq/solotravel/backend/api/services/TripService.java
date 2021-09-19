package ar.edu.unq.solotravel.backend.api.services;

import ar.edu.unq.solotravel.backend.api.models.Trip;
import ar.edu.unq.solotravel.backend.api.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    public ResponseEntity getAllTrips() {

        List<Trip> allTrips = tripRepository.findAll();
        return ResponseEntity.ok().body(allTrips);
    }
}
