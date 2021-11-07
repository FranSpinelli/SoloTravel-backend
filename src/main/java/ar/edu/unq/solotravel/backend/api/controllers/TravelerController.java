package ar.edu.unq.solotravel.backend.api.controllers;

import ar.edu.unq.solotravel.backend.api.exceptions.NoSuchElementException;
import ar.edu.unq.solotravel.backend.api.security.ValidateGoogleJwt;
import ar.edu.unq.solotravel.backend.api.services.TripService;
import ar.edu.unq.solotravel.backend.api.services.TravelerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/travelers")
public class TravelerController {

    @Autowired
    private TravelerService travelerService;

    @Autowired
    private TripService tripService;

    @ValidateGoogleJwt
    @GetMapping("/{userId}/favorites")
    public ResponseEntity getUserFavorites(
            @RequestHeader("Authorization") String googleToken,
            @PathVariable Integer userId) {
        return ResponseEntity.ok().body(travelerService.getUserFavorites(userId));
    }

    @ValidateGoogleJwt
    @PutMapping("/{userId}/favorites/{tripId}")
    public ResponseEntity addTripToUserFavorites(
            @RequestHeader("Authorization") String googleToken,
            @PathVariable Integer userId,
            @PathVariable Integer tripId) {
        travelerService.addTripToUserFavorites(userId, tripId);
        return ResponseEntity.ok().build();
    }

    @ValidateGoogleJwt
    @DeleteMapping("/{userId}/favorites/{tripId}")
    public ResponseEntity removeTripFromUserFavorites(
            @RequestHeader("Authorization") String googleToken,
            @PathVariable Integer userId,
            @PathVariable Integer tripId) {
        travelerService.removeTripFromUserFavorites(userId, tripId);
        return ResponseEntity.ok().build();
    }

    @ValidateGoogleJwt
    @GetMapping("/{userId}")
    public ResponseEntity getTravelerTripsConsideringFavourites(
            @RequestHeader("Authorization") String googleToken,
            @PathVariable Integer userId,
            @RequestParam(required = false) String name) throws NoSuchElementException {
        return ResponseEntity.ok().body(tripService.getAllTripsByUser(userId, name));
    }
}
