package ar.edu.unq.solotravel.backend.api.controllers;

import ar.edu.unq.solotravel.backend.api.exceptions.NoSuchElementException;
import ar.edu.unq.solotravel.backend.api.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping()
    public ResponseEntity getAllTrips(@RequestParam(required = false) String name) {
        return ResponseEntity.ok().body(tripService.getAllTrips(name));
    }

    @GetMapping("/{tripId}")
    public ResponseEntity getTripById(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tripId) throws NoSuchElementException {
        return ResponseEntity.ok().body(tripService.getTripById(tripId));
    }
}
