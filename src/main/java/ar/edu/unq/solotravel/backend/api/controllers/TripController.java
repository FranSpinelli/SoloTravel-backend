package ar.edu.unq.solotravel.backend.api.controllers;

import ar.edu.unq.solotravel.backend.api.dtos.CreateTripDto;
import ar.edu.unq.solotravel.backend.api.dtos.TravelAgencyLoginDto;
import ar.edu.unq.solotravel.backend.api.exceptions.NoSuchElementException;
import ar.edu.unq.solotravel.backend.api.security.ValidateGoogleJwt;
import ar.edu.unq.solotravel.backend.api.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @ValidateGoogleJwt
    @GetMapping("/{userId}")
    public ResponseEntity getAllTripsByUser(
            @RequestHeader("Authorization") String googleToken,
            @PathVariable Integer userId,
            @RequestParam(required = false) String name) throws NoSuchElementException {
        return ResponseEntity.ok().body(tripService.getAllTripsByUser(userId, name));
    }

    @PostMapping("/{agencyId}/new")
    public ResponseEntity createTrip(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer agencyId,
            @RequestBody CreateTripDto createTripDto) {
        tripService.createTrip(agencyId, createTripDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}
