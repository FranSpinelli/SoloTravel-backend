package ar.edu.unq.solotravel.backend.api.controllers;

import ar.edu.unq.solotravel.backend.api.dtos.CreateTripDto;
import ar.edu.unq.solotravel.backend.api.dtos.UpdateTripDto;
import ar.edu.unq.solotravel.backend.api.services.TripService;
import ar.edu.unq.solotravel.backend.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/agency")
public class TravelAgencyController {

    @Autowired
    private TripService tripService;

    @Autowired
    private UserService userService;

    @PostMapping("/{agencyId}/new")
    public ResponseEntity createTrip(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer agencyId,
            @RequestBody CreateTripDto createTripDto) {
        tripService.createTrip(agencyId, createTripDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/{agencyId}/edition/{tripId}")
    public ResponseEntity updateTrip(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer agencyId,
            @PathVariable Integer tripId,
            @RequestBody UpdateTripDto updateTripDto) {
        tripService.updateTrip(agencyId, tripId, updateTripDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{agencyId}/deletion/{tripId}")
    public ResponseEntity deleteTrip(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer agencyId,
            @PathVariable Integer tripId) {
        tripService.deleteTrip(agencyId, tripId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{agencyId}/trips")
    public ResponseEntity getAgencyTrips(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer agencyId) {
        return ResponseEntity.ok().body(userService.getAgencyTrips(agencyId));
    }
}
