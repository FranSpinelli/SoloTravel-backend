package ar.edu.unq.solotravel.backend.api.services;

import ar.edu.unq.solotravel.backend.api.dtos.GoogleAuthResponseDto;
import ar.edu.unq.solotravel.backend.api.dtos.GoogleProfileDto;
import ar.edu.unq.solotravel.backend.api.dtos.LoginDto;
import ar.edu.unq.solotravel.backend.api.models.Traveler;
import ar.edu.unq.solotravel.backend.api.repositories.TravelerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private TravelerRepository travelerRepository;

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

    public ResponseEntity authenticateTravelAgent(LoginDto loginDto) {
       //Optional<TravelAgent> travelAgentWithEmail =
        return null;
    }
}
