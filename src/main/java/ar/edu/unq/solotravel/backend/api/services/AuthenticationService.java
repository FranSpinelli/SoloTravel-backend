package ar.edu.unq.solotravel.backend.api.services;

import ar.edu.unq.solotravel.backend.api.dtos.GoogleAuthResponseDto;
import ar.edu.unq.solotravel.backend.api.dtos.GoogleProfileDto;
import ar.edu.unq.solotravel.backend.api.dtos.TokenResponseDto;
import ar.edu.unq.solotravel.backend.api.dtos.TravelAgencyLoginDto;
import ar.edu.unq.solotravel.backend.api.exceptions.LogInException;
import ar.edu.unq.solotravel.backend.api.helpers.InternalJwtHelper;
import ar.edu.unq.solotravel.backend.api.models.TravelAgency;
import ar.edu.unq.solotravel.backend.api.models.Traveler;
import ar.edu.unq.solotravel.backend.api.repositories.TravelAgencyRepository;
import ar.edu.unq.solotravel.backend.api.repositories.TravelerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private TravelerRepository travelerRepository;
    @Autowired
    private TravelAgencyRepository travelAgencyRepository;
    @Autowired
    private InternalJwtHelper internalJwtHelper;

    public GoogleAuthResponseDto authenticateByGoogle(GoogleProfileDto profileDto) {
        Optional<Traveler> existentUser = travelerRepository.findByGoogleId(profileDto.getGoogleId());
        // if user does not exist, save it
        if (!existentUser.isPresent()) {
            // User newUser = modelMapper.map(profileDto, User.class);
            Traveler newUser = new Traveler();
            newUser.setGoogleId(profileDto.getGoogleId());
            newUser.setName(profileDto.getName());
            newUser.setEmail(profileDto.getEmail());
            newUser.setPicture(profileDto.getPicture());

            return new GoogleAuthResponseDto(travelerRepository.save(newUser).getId());
        }
        return new GoogleAuthResponseDto(existentUser.get().getId());
    }

    public TokenResponseDto authenticateTravelAgency(TravelAgencyLoginDto travelAgencyLoginDto) {
        TravelAgency travelAgencyWithEmail = travelAgencyRepository.findByEmail(travelAgencyLoginDto.getEmail())
               .orElseThrow(() -> new LogInException("Incorrect mail or password"));

       if(!travelAgencyWithEmail.getPassword().equals(travelAgencyLoginDto.getPassword())){
           throw new LogInException("Incorrect mail or password");
       }

       String token = internalJwtHelper.getTokenFor(travelAgencyWithEmail.getEmail());
       return new TokenResponseDto(token, travelAgencyWithEmail.getName(), travelAgencyWithEmail.getId());
    }
}
