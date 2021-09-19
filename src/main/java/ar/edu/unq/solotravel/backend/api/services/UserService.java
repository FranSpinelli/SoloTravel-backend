package ar.edu.unq.solotravel.backend.api.services;

import ar.edu.unq.solotravel.backend.api.exceptions.NoSuchElementException;
import ar.edu.unq.solotravel.backend.api.models.Trip;
import ar.edu.unq.solotravel.backend.api.models.User;
import ar.edu.unq.solotravel.backend.api.repositories.TripRepository;
import ar.edu.unq.solotravel.backend.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TripRepository tripRepository;

    public ResponseEntity getUserFavorites(Integer userId) throws NoSuchElementException {

        User userWithId = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User with Id: " + userId));
        return ResponseEntity.ok().body(userWithId.getFavorites());
    }

    public ResponseEntity addTripToUserFavorites(Integer userId, Integer tripId) throws NoSuchElementException {

        User userWithId = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User with Id: " + userId));
        Trip tripWithId = tripRepository.findById(tripId).orElseThrow(() -> new NoSuchElementException("No Trip with Id: " + tripId));
        userWithId.addFavorite(tripWithId);
        userRepository.save(userWithId);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity removeTripFromUserFavorites(Integer userId, Integer tripId) throws NoSuchElementException {

        User userWithId = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User with Id: " + userId));
        Trip tripWithId = tripRepository.findById(tripId).orElseThrow(() -> new NoSuchElementException("No Trip with Id: " + tripId));
        userWithId.removeFavorite(tripWithId);
        userRepository.save(userWithId);

        return ResponseEntity.ok().build();
    }
}
