package ar.edu.unq.solotravel.backend.api;

import ar.edu.unq.solotravel.backend.api.dtos.CreateTripDto;
import ar.edu.unq.solotravel.backend.api.dtos.TripDto;
import ar.edu.unq.solotravel.backend.api.models.Trip;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class BackendSoloTravelApiApplication {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/soloTravel");
		SpringApplication.run(BackendSoloTravelApiApplication.class, args);
	}

	@Bean
	public ModelMapper getModelMapper() {

		ModelMapper modelMapper = new ModelMapper();

		modelMapper.typeMap(Trip.class, TripDto.class).addMappings(mapper -> {
			mapper.map(Trip::getDuration, TripDto::setDuration);
		});

		modelMapper.typeMap(CreateTripDto.class, Trip.class).addMappings(mapper -> {
			mapper.map(CreateTripDto::getTotalSlots, Trip::setAvailableSlots);
		});

		return modelMapper;
	}
}
