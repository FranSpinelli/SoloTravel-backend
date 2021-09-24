package ar.edu.unq.solotravel.backend.api.services;

import ar.edu.unq.solotravel.backend.api.dtos.TripDto;
import ar.edu.unq.solotravel.backend.api.dtos.TripListResponseDto;
import ar.edu.unq.solotravel.backend.api.exceptions.NoSuchElementException;
import ar.edu.unq.solotravel.backend.api.models.Trip;
import ar.edu.unq.solotravel.backend.api.models.User;
import ar.edu.unq.solotravel.backend.api.repositories.TripRepository;
import ar.edu.unq.solotravel.backend.api.repositories.UserRepository;
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
    private UserRepository userRepository;
    @Autowired
    private TripSpecsBuilder tripSpecsBuilder;
    @Autowired
    private ModelMapper modelMapper;

    public TripListResponseDto getAllTrips(Integer userId, String name) throws NoSuchElementException {

        Specification<Trip> specs = tripSpecsBuilder.buildCriteriaSpecs(name);
        User userWithId = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User with Id: " + userId));

        List<TripDto> tripsDtoList = tripRepository.findAll(specs).stream().map( trip -> modelMapper.map(trip, TripDto.class)).collect(Collectors.toList());
        // TODO: Enhance the way of checking if it is a favorite trip
        tripsDtoList.forEach(tripDto -> {
            if (userWithId.getFavorites().stream()
                    .anyMatch(trip -> tripDto.getId().equals(trip.getId()))) {
                tripDto.setIsFavorite(true);
            }
        });

        return new TripListResponseDto(tripsDtoList);
    }
}
