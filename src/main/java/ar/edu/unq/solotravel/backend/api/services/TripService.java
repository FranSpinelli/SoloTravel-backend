package ar.edu.unq.solotravel.backend.api.services;

import ar.edu.unq.solotravel.backend.api.dtos.*;
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

import java.time.LocalDate;
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

    public TripListResponseDto getAllTrips(SearchTripParamsDto params) {

        Specification<Trip> specs = tripSpecsBuilder.buildCriteriaSpecs(params);
        List<TripDto> tripsDtoList = tripRepository.findAll(specs).stream().map( trip -> modelMapper.map(trip, TripDto.class)).collect(Collectors.toList());

        return new TripListResponseDto(tripsDtoList);
    }

    public TripListResponseDto getAllTripsByUser(Integer userId, SearchTripParamsDto params) throws NoSuchElementException {

        Traveler userWithId = travelerRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User with Id: " + userId));

        TripListResponseDto tripsDtoList = this.getAllTrips(params);
        tripsDtoList = setFavoritesTripsFromUser(tripsDtoList.getTrips(), userWithId.getFavorites());

        return tripsDtoList;
    }

    public void createTrip(Integer agencyId, CreateTripDto createTripDto) {
        TravelAgency agencyWithId = travelAgencyRepository.findById(agencyId).orElseThrow(() -> new NoSuchElementException("No Agency with Id: " + agencyId));

        Trip newTrip = modelMapper.map(createTripDto, Trip.class);
        tripRepository.save(newTrip);

        agencyWithId.addTrip(newTrip);
        travelAgencyRepository.save(agencyWithId);
    }

    public void updateTrip(Integer agencyId, Integer tripId, UpdateTripDto updateTripDto) {
        TravelAgency agencyWithId = travelAgencyRepository.findById(agencyId).orElseThrow(() -> new NoSuchElementException("No Agency with Id: " + agencyId));
        if (agencyWithId.getTrips().stream().anyMatch(trip -> trip.getId().equals(tripId)) && tripId.equals(updateTripDto.getId())){
            Trip tripWithId = tripRepository.findById(tripId).orElseThrow(() -> new NoSuchElementException("No Trip with Id: " + tripId));
            tripWithId.setName(updateTripDto.getName());
            tripWithId.setDestination(updateTripDto.getDestination());
            tripWithId.setImage(updateTripDto.getImage());
            tripWithId.setDescription(updateTripDto.getDescription());
            tripWithId.setPrice(updateTripDto.getPrice());
            tripWithId.setStartDate(updateTripDto.getStartDate());
            tripWithId.setEndDate(updateTripDto.getEndDate());
            tripWithId.setTotalSlots(updateTripDto.getTotalSlots());
            tripWithId.setAvailableSlots(updateTripDto.getTotalSlots());
            tripRepository.save(tripWithId);
        }else{
            throw new NoSuchElementException("The Agency does not contain a trip with Id: " + tripId);
        }
    }

    public TripDetailsDto getTripById(Integer tripId) throws NoSuchElementException {
        Trip tripWithId = tripRepository.findById(tripId).orElseThrow(() -> new NoSuchElementException("No Trip with Id: " + tripId));

        return modelMapper.map(tripWithId, TripDetailsDto.class);
    }

    public AgencyTripDetailsDto getTripDetails(Integer tripId) throws NoSuchElementException {
        TravelAgency agency = travelAgencyRepository.findByTripsId(tripId).orElseThrow(() -> new NoSuchElementException("No trip with id: " + tripId));
        Trip trip = agency.getTrips().stream().filter(t -> t.getId().equals(tripId)).findFirst().get();

        AgencyTripDetailsDto result = modelMapper.map(trip, AgencyTripDetailsDto.class);
        result.setAgencyId(agency.getId());
        result.setAgencyName(agency.getName());

        return result;
    }

    public void deleteTrip(Integer agencyId, Integer tripId) {

        TravelAgency agencyWithId = travelAgencyRepository.findById(agencyId).orElseThrow(() -> new NoSuchElementException("No Agency with Id: " + agencyId));
        Trip tripWithId = tripRepository.findById(tripId).orElseThrow(() -> new NoSuchElementException("No Trip with Id: " + tripId));

        if (agencyWithId.getTrips().stream().anyMatch(trip -> trip.getId().equals(tripId))){
            agencyWithId.removeTrip(tripWithId);
            tripRepository.deleteById(tripId);
        }else{
            throw new NoSuchElementException("The Agency does not contain a trip with Id: " + tripId);
        }
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
