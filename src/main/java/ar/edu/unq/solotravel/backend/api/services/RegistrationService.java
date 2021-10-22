package ar.edu.unq.solotravel.backend.api.services;

import ar.edu.unq.solotravel.backend.api.dtos.TravelAgencyRegisterDto;
import ar.edu.unq.solotravel.backend.api.exceptions.LogInException;
import ar.edu.unq.solotravel.backend.api.exceptions.RegisterException;
import ar.edu.unq.solotravel.backend.api.models.TravelAgency;
import ar.edu.unq.solotravel.backend.api.repositories.TravelAgencyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationService {

    @Autowired
    private TravelAgencyRepository travelAgencyRepository;
    @Autowired
    private ModelMapper modelMapper;

    public void registerTravelAgency(TravelAgencyRegisterDto travelAgencyRegisterDto) {

        Optional<TravelAgency> travelAgencyWithEmail = travelAgencyRepository.findByEmail(travelAgencyRegisterDto.getEmail());

        if (travelAgencyWithEmail.isPresent()) {
            throw new RegisterException("There is already a Travel Agency using that email");
        }
        TravelAgency newTravelAgency =  modelMapper.map(travelAgencyRegisterDto, TravelAgency.class);
        travelAgencyRepository.save(newTravelAgency);
    }

}
