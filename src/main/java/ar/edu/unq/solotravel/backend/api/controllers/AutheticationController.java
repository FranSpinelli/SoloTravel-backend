package ar.edu.unq.solotravel.backend.api.controllers;

import ar.edu.unq.solotravel.backend.api.dtos.ErrorResponseDto;
import ar.edu.unq.solotravel.backend.api.dtos.GoogleAuthResponseDto;
import ar.edu.unq.solotravel.backend.api.dtos.GoogleProfileDto;
import ar.edu.unq.solotravel.backend.api.dtos.LoginDto;
import ar.edu.unq.solotravel.backend.api.helpers.JwtHelper;
import ar.edu.unq.solotravel.backend.api.services.AuthenticationService;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/authentication")
public class AutheticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/login/google")
    public ResponseEntity authenticateByGoogle(
            @RequestHeader("Authorization") String googleToken,
            @RequestBody GoogleProfileDto googleProfileDto
    ) {
        DecodedJWT jwt = jwtHelper.verifyGoogleToken(googleToken);
        if (jwt == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDto("Invalid authentication token"));

        GoogleAuthResponseDto res = authenticationService.authenticateByGoogle(googleProfileDto);

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/login/internal")
    public ResponseEntity authenticatieTravelAgent(@RequestBody LoginDto loginDto){
        return authenticationService.authenticateTravelAgent(loginDto);
    }
}
