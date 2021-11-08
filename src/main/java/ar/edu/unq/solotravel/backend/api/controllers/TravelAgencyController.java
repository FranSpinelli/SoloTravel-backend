package ar.edu.unq.solotravel.backend.api.controllers;

import ar.edu.unq.solotravel.backend.api.dtos.CreateTripDto;
import ar.edu.unq.solotravel.backend.api.dtos.UpdateTripDto;
import ar.edu.unq.solotravel.backend.api.security.ValidateInternalJwt;
import ar.edu.unq.solotravel.backend.api.services.TravelAgencyService;
import ar.edu.unq.solotravel.backend.api.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/agencies")
public class TravelAgencyController {

    @Autowired
    private TripService tripService;

    @Autowired
    private TravelAgencyService travelAgencyService;

    @ValidateInternalJwt
    @PostMapping("/{agencyId}/new")
    public ResponseEntity createTrip(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer agencyId,
            @RequestBody CreateTripDto createTripDto) {
        tripService.createTrip(agencyId, createTripDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @ValidateInternalJwt
    @PutMapping("/{agencyId}/edition/{tripId}")
    public ResponseEntity updateTrip(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer agencyId,
            @PathVariable Integer tripId,
            @RequestBody UpdateTripDto updateTripDto) {
        tripService.updateTrip(agencyId, tripId, updateTripDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ValidateInternalJwt
    @DeleteMapping("/{agencyId}/deletion/{tripId}")
    public ResponseEntity deleteTrip(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer agencyId,
            @PathVariable Integer tripId) {
        tripService.deleteTrip(agencyId, tripId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ValidateInternalJwt
    @GetMapping("/{agencyId}/trips")
    public ResponseEntity getAgencyTrips(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer agencyId) {
        return ResponseEntity.ok().body(travelAgencyService.getAgencyTrips(agencyId));
    }
}
