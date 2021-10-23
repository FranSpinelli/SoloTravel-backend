package ar.edu.unq.solotravel.backend.api.controllers;

import ar.edu.unq.solotravel.backend.api.dtos.TravelAgencyRegisterDto;
import ar.edu.unq.solotravel.backend.api.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @PostMapping("/register/travelAgency")
    public ResponseEntity registerTravelAgency(@RequestBody @Valid TravelAgencyRegisterDto travelAgencyRegisterDto){
        registrationService.registerTravelAgency(travelAgencyRegisterDto);
        return ResponseEntity.ok().build();
    }
}
