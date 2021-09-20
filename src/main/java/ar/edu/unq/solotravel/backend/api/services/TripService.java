package ar.edu.unq.solotravel.backend.api.services;

import ar.edu.unq.solotravel.backend.api.dtos.TripDto;
import ar.edu.unq.solotravel.backend.api.dtos.TripListResponseDto;
import ar.edu.unq.solotravel.backend.api.repositories.TripRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    public TripListResponseDto getAllTrips() {

        ModelMapper modelMapper = new ModelMapper();
        List<TripDto> tripsDtoList = tripRepository.findAll().stream().map( trip -> modelMapper.map(trip, TripDto.class)).collect(Collectors.toList());

        return new TripListResponseDto(tripsDtoList);
    }
}
