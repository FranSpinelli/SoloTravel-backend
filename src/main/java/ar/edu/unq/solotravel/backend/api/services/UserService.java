package ar.edu.unq.solotravel.backend.api.services;

import ar.edu.unq.solotravel.backend.api.exceptions.NoSuchElementException;
import ar.edu.unq.solotravel.backend.api.models.Trip;
import ar.edu.unq.solotravel.backend.api.models.User;
import ar.edu.unq.solotravel.backend.api.repositories.TripRepository;
import ar.edu.unq.solotravel.backend.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TripRepository tripRepository;

    public List<Trip> getUserFavorites(Integer userId) throws NoSuchElementException {

        User userWithId = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User with Id: " + userId));
        return userWithId.getFavorites();
    }

    public void addTripToUserFavorites(Integer userId, Integer tripId) throws NoSuchElementException {

        User userWithId = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User with Id: " + userId));
        Trip tripWithId = tripRepository.findById(tripId).orElseThrow(() -> new NoSuchElementException("No Trip with Id: " + tripId));
        userWithId.addFavorite(tripWithId);
        userRepository.save(userWithId);
    }

    public void removeTripFromUserFavorites(Integer userId, Integer tripId) throws NoSuchElementException {

        User userWithId = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User with Id: " + userId));
        Trip tripWithId = tripRepository.findById(tripId).orElseThrow(() -> new NoSuchElementException("No Trip with Id: " + tripId));
        userWithId.removeFavorite(tripWithId);
        userRepository.save(userWithId);
    }
}
