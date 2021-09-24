package ar.edu.unq.solotravel.backend.api.services;

import ar.edu.unq.solotravel.backend.api.dtos.TripDto;
import ar.edu.unq.solotravel.backend.api.dtos.TripListResponseDto;
import ar.edu.unq.solotravel.backend.api.exceptions.NoSuchElementException;
import ar.edu.unq.solotravel.backend.api.models.Trip;
import ar.edu.unq.solotravel.backend.api.models.User;
import ar.edu.unq.solotravel.backend.api.repositories.TripRepository;
import ar.edu.unq.solotravel.backend.api.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ModelMapper modelMapper;

    public TripListResponseDto getUserFavorites(Integer userId) throws NoSuchElementException {

        User userWithId = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User with Id: " + userId));

        List<TripDto> tripsDtoList = userWithId.getFavorites().stream().map(trip -> modelMapper.map(trip, TripDto.class)).collect(Collectors.toList());

        return new TripListResponseDto(tripsDtoList);
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
