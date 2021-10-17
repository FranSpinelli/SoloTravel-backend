package ar.edu.unq.solotravel.backend.api.controllers;

import ar.edu.unq.solotravel.backend.api.dtos.GoogleAuthResponseDto;
import ar.edu.unq.solotravel.backend.api.dtos.GoogleProfileDto;
import ar.edu.unq.solotravel.backend.api.dtos.TravelAgencyLoginDto;
import ar.edu.unq.solotravel.backend.api.helpers.GoogleJwtHelper;
import ar.edu.unq.solotravel.backend.api.security.ValidateGoogleJwt;
import ar.edu.unq.solotravel.backend.api.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/authentication")
public class AutheticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private GoogleJwtHelper googleJwtHelper;

    @ValidateGoogleJwt
    @PostMapping("/login/google")
    public ResponseEntity authenticateByGoogle(@RequestHeader("Authorization") String googleToken) {
        GoogleProfileDto profileDto = googleJwtHelper.getProfileInfo(googleToken);
        GoogleAuthResponseDto res = authenticationService.authenticateByGoogle(profileDto);

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/login/internal")
    public ResponseEntity authenticatieTravelAgency(@RequestBody TravelAgencyLoginDto travelAgencyLoginDto) {
        return ResponseEntity.ok().body(authenticationService.authenticateTravelAgency(travelAgencyLoginDto));
    }
}
