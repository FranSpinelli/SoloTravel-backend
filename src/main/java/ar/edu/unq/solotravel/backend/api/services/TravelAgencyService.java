package ar.edu.unq.solotravel.backend.api.services;

import ar.edu.unq.solotravel.backend.api.dtos.TripDto;
import ar.edu.unq.solotravel.backend.api.dtos.TripListResponseDto;
import ar.edu.unq.solotravel.backend.api.exceptions.NoSuchElementException;
import ar.edu.unq.solotravel.backend.api.models.TravelAgency;
import ar.edu.unq.solotravel.backend.api.repositories.TravelAgencyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TravelAgencyService {

    @Autowired
    private TravelAgencyRepository travelAgencyRepository;
    @Autowired
    private ModelMapper modelMapper;

    public TripListResponseDto getAgencyTrips(Integer agencyId) {

        TravelAgency agencyWithId = travelAgencyRepository.findById(agencyId).orElseThrow(() -> new NoSuchElementException("No Agency with Id: " + agencyId));

        List<TripDto> tripsDtoList = agencyWithId.getTrips().stream().map(trip -> modelMapper.map(trip, TripDto.class)).collect(Collectors.toList());

        return new TripListResponseDto(tripsDtoList);
    }

}
