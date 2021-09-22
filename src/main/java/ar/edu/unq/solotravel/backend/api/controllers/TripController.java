package ar.edu.unq.solotravel.backend.api.controllers;

import ar.edu.unq.solotravel.backend.api.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping("/trips")
    public ResponseEntity getAllTrips(@RequestParam(required = false) String name){
        return ResponseEntity.ok().body(tripService.getAllTrips(name));
    }
}
