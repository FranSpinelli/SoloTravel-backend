package ar.edu.unq.solotravel.backend.api.services;

import ar.edu.unq.solotravel.backend.api.dtos.GoogleAuthResponseDto;
import ar.edu.unq.solotravel.backend.api.dtos.GoogleProfileDto;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ModelMapper modelMapper;

    public GoogleAuthResponseDto authenticateByGoogle(GoogleProfileDto profileDto) {
        Optional<User> existentUser = userRepository.findByGoogleId(profileDto.getGoogleId());
        // if user does not exist, save it
        if (!existentUser.isPresent()) {
            // User newUser = modelMapper.map(profileDto, User.class);
            User newUser = new User();
            newUser.setGoogleId(profileDto.getGoogleId());
            newUser.setName(profileDto.getName());
            newUser.setEmail(profileDto.getEmail());
            newUser.setPicture(profileDto.getPicture());

            return new GoogleAuthResponseDto(userRepository.save(newUser).getId());
        }
        return new GoogleAuthResponseDto(existentUser.get().getId());
    }

    public TripListResponseDto getUserFavorites(Integer userId) throws NoSuchElementException {

        User userWithId = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User with Id: " + userId));

        List<TripDto> tripsDtoList = userWithId.getFavorites().stream().map(trip -> modelMapper.map(trip, TripDto.class)).collect(Collectors.toList());
        // TODO: Enhance the way of setting a trip as favorite (ideally using model mapper)
        tripsDtoList.forEach(tripDto -> tripDto.setIsFavorite(true));

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
