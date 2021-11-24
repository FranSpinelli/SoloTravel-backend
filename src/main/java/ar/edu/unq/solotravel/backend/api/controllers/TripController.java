package ar.edu.unq.solotravel.backend.api.controllers;

import ar.edu.unq.solotravel.backend.api.dtos.SearchTripParamsDto;
import ar.edu.unq.solotravel.backend.api.exceptions.NoSuchElementException;
import ar.edu.unq.solotravel.backend.api.security.ValidateInternalJwt;
import ar.edu.unq.solotravel.backend.api.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping()
    public ResponseEntity getAllTrips(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String date) {
        LocalDate searchDate = date == null ? LocalDate.now() : LocalDate.parse(date.substring(0, 10));
        if (searchDate.isBefore(LocalDate.now()))
            searchDate = LocalDate.now();
        SearchTripParamsDto paramsDto = new SearchTripParamsDto(destination, searchDate);
        return ResponseEntity.ok().body(tripService.getAllTrips(paramsDto));
    }

    @ValidateInternalJwt
    @GetMapping("/{tripId}")
    public ResponseEntity getTripById(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tripId) throws NoSuchElementException {
        return ResponseEntity.ok().body(tripService.getTripById(tripId));
    }

    @GetMapping("/{tripId}/details")
    public ResponseEntity getTripDetails(@PathVariable Integer tripId) {
        return ResponseEntity.ok().body(tripService.getTripDetails(tripId));
    }
}
