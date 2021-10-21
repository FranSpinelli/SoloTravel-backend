package ar.edu.unq.solotravel.backend.api.services;

import ar.edu.unq.solotravel.backend.api.dtos.TripDto;
import ar.edu.unq.solotravel.backend.api.dtos.TripListResponseDto;
import ar.edu.unq.solotravel.backend.api.exceptions.NoSuchElementException;
import ar.edu.unq.solotravel.backend.api.models.TravelAgency;
import ar.edu.unq.solotravel.backend.api.models.Traveler;
import ar.edu.unq.solotravel.backend.api.models.Trip;
import ar.edu.unq.solotravel.backend.api.repositories.TravelerRepository;
import ar.edu.unq.solotravel.backend.api.repositories.TravelAgencyRepository;
import ar.edu.unq.solotravel.backend.api.repositories.TripRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private TravelerRepository travelerRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private TravelAgencyRepository travelAgencyRepository;
    @Autowired
    private ModelMapper modelMapper;

    public TripListResponseDto getUserFavorites(Integer userId) {

        Traveler userWithId = travelerRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User with Id: " + userId));

        List<TripDto> tripsDtoList = userWithId.getFavorites().stream().map(trip -> modelMapper.map(trip, TripDto.class)).collect(Collectors.toList());
        // TODO: Enhance the way of setting a trip as favorite (ideally using model mapper)
        tripsDtoList.forEach(tripDto -> tripDto.setIsFavorite(true));

        return new TripListResponseDto(tripsDtoList);
    }

    public void addTripToUserFavorites(Integer userId, Integer tripId) {

        Traveler userWithId = travelerRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User with Id: " + userId));
        Trip tripWithId = tripRepository.findById(tripId).orElseThrow(() -> new NoSuchElementException("No Trip with Id: " + tripId));
        userWithId.addFavorite(tripWithId);
        travelerRepository.save(userWithId);
    }

    public void removeTripFromUserFavorites(Integer userId, Integer tripId) {

        Traveler userWithId = travelerRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User with Id: " + userId));
        Trip tripWithId = tripRepository.findById(tripId).orElseThrow(() -> new NoSuchElementException("No Trip with Id: " + tripId));
        userWithId.removeFavorite(tripWithId);
        travelerRepository.save(userWithId);
    }

    public TripListResponseDto getAgencyTrips(Integer agencyId) {

        TravelAgency agencyWithId = travelAgencyRepository.findById(agencyId).orElseThrow(() -> new NoSuchElementException("No Agency with Id: " + agencyId));

        List<TripDto> tripsDtoList = agencyWithId.getTrips().stream().map(trip -> modelMapper.map(trip, TripDto.class)).collect(Collectors.toList());

        return new TripListResponseDto(tripsDtoList);
    }
}
