package ar.edu.unq.solotravel.backend.api.controllers;

import ar.edu.unq.solotravel.backend.api.exceptions.NoSuchElementException;
import ar.edu.unq.solotravel.backend.api.security.ValidateGoogleJwt;
import ar.edu.unq.solotravel.backend.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ValidateGoogleJwt
    @GetMapping("/{userId}/favorites")
    public ResponseEntity getUserFavorites(
            @RequestHeader("Authorization") String googleToken,
            @PathVariable Integer userId) throws NoSuchElementException {
        return ResponseEntity.ok().body(userService.getUserFavorites(userId));
    }

    @ValidateGoogleJwt
    @PutMapping("/{userId}/favorites/{tripId}")
    public ResponseEntity addTripToUserFavorites(
            @RequestHeader("Authorization") String googleToken,
            @PathVariable Integer userId,
            @PathVariable Integer tripId) throws NoSuchElementException {
        userService.addTripToUserFavorites(userId, tripId);
        return ResponseEntity.ok().build();
    }

    @ValidateGoogleJwt
    @DeleteMapping("/{userId}/favorites/{tripId}")
    public ResponseEntity removeTripFromUserFavorites(
            @RequestHeader("Authorization") String googleToken,
            @PathVariable Integer userId,
            @PathVariable Integer tripId) throws NoSuchElementException{
        userService.removeTripFromUserFavorites(userId, tripId);
        return ResponseEntity.ok().build();
    }
}
