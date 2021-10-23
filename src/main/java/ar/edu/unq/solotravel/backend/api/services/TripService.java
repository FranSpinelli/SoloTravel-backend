package ar.edu.unq.solotravel.backend.api.services;

import ar.edu.unq.solotravel.backend.api.dtos.CreateTripDto;
import ar.edu.unq.solotravel.backend.api.dtos.TripDto;
import ar.edu.unq.solotravel.backend.api.dtos.TripListResponseDto;
import ar.edu.unq.solotravel.backend.api.exceptions.NoSuchElementException;
import ar.edu.unq.solotravel.backend.api.models.TravelAgency;
import ar.edu.unq.solotravel.backend.api.models.Traveler;
import ar.edu.unq.solotravel.backend.api.models.Trip;
import ar.edu.unq.solotravel.backend.api.repositories.TravelAgencyRepository;
import ar.edu.unq.solotravel.backend.api.repositories.TravelerRepository;
import ar.edu.unq.solotravel.backend.api.repositories.TripRepository;
import ar.edu.unq.solotravel.backend.api.specifications.TripSpecsBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private TravelerRepository travelerRepository;
    @Autowired
    private TravelAgencyRepository travelAgencyRepository;
    @Autowired
    private TripSpecsBuilder tripSpecsBuilder;
    @Autowired
    private ModelMapper modelMapper;

    public TripListResponseDto getAllTrips(String name) {

        Specification<Trip> specs = tripSpecsBuilder.buildCriteriaSpecs(name);
        List<TripDto> tripsDtoList = tripRepository.findAll(specs).stream().map( trip -> modelMapper.map(trip, TripDto.class)).collect(Collectors.toList());

        return new TripListResponseDto(tripsDtoList);
    }

    public TripListResponseDto getAllTripsByUser(Integer userId, String name) throws NoSuchElementException {

        Traveler userWithId = travelerRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User with Id: " + userId));

        TripListResponseDto tripsDtoList = this.getAllTrips(name);
        tripsDtoList = setFavoritesTripsFromUser(tripsDtoList.getTrips(), userWithId.getFavorites());

        return tripsDtoList;
    }

    public void createTrip(Integer agencyId, CreateTripDto createTripDto) {
        TravelAgency agencyWithId = travelAgencyRepository.findById(agencyId).orElseThrow(() -> new NoSuchElementException("No Agency with Id: " + agencyId));

        Trip newTrip = modelMapper.map(createTripDto, Trip.class);
        agencyWithId.addTrip(newTrip);

        travelAgencyRepository.save(agencyWithId);
    }

    private TripListResponseDto setFavoritesTripsFromUser(List<TripDto> trips, List<Trip> userFavorites) {
        trips.forEach(tripDto -> {
            if (userFavorites.stream().anyMatch(trip -> tripDto.getId().equals(trip.getId()))) {
                tripDto.setIsFavorite(true);
            }
        });
        return new TripListResponseDto(trips);
    }
}
