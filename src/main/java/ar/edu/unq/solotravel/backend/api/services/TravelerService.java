package ar.edu.unq.solotravel.backend.api.services;

import ar.edu.unq.solotravel.backend.api.dtos.MailDto;
import ar.edu.unq.solotravel.backend.api.dtos.TripDto;
import ar.edu.unq.solotravel.backend.api.dtos.TripListResponseDto;
import ar.edu.unq.solotravel.backend.api.exceptions.InvalidActionException;
import ar.edu.unq.solotravel.backend.api.exceptions.NoSuchElementException;
import ar.edu.unq.solotravel.backend.api.helpers.EmailSenderHelper;
import ar.edu.unq.solotravel.backend.api.models.TravelAgency;
import ar.edu.unq.solotravel.backend.api.models.Traveler;
import ar.edu.unq.solotravel.backend.api.models.Trip;
import ar.edu.unq.solotravel.backend.api.repositories.TravelerRepository;
import ar.edu.unq.solotravel.backend.api.repositories.TravelAgencyRepository;
import ar.edu.unq.solotravel.backend.api.repositories.TripRepository;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TravelerService {

    @Autowired
    private TravelerRepository travelerRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private TravelAgencyRepository travelAgencyRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmailSenderHelper emailSenderHelper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Queue mailQueue;


    public TripListResponseDto getUserFavorites(Integer userId) {

        Traveler userWithId = travelerRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User with Id: " + userId));

        List<TripDto> tripsDtoList = userWithId.getFavorites().stream().map(trip -> modelMapper.map(trip, Trip.class))
                .filter(trip -> trip.getStartDate().isAfter(LocalDate.now()))
                .map(trip -> modelMapper.map(trip, TripDto.class)).collect(Collectors.toList());

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

    public void bookTrip(Integer userId, Integer tripId) {

        Traveler userWithId = travelerRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User with Id: " + userId));
        Trip tripWithId = tripRepository.findById(tripId).orElseThrow(() -> new NoSuchElementException("No Trip with Id: " + tripId));
        TravelAgency travelAgencyWithTrip = travelAgencyRepository.findByTripsId(tripId).orElseThrow(() -> new NoSuchElementException("No trip with id: " + tripId));;

        String travelerEmailSubject = "SoloApp: reserva confirmada a \"" + tripWithId.getName() + "\"";
        String travelerEmailBody = "Se ha confirmado su reserva para el viaje, un miembro de " + travelAgencyWithTrip.getName() + " se estará comunicando con usted para ultimar detalles.";

        String travelAgencyEmailSubject = "SoloApp: se ha realizado una reserva en el viaje \"" + tripWithId.getName() + "\"";
        String travelAgencyEmailBOdy = "El pasajero ya fue notificado de la reserva y esta esperando ser contactado por usted para ultimar detalles. \n mail de contacto: " + userWithId.getEmail();

        if(! tripWithId.hasAvailableSlot()){
            throw new InvalidActionException("Trip " + tripId + " hasn't got any available slot");
        }else if (userWithId.hasBookedTrip(tripWithId)){
            throw new InvalidActionException("User " + userId + " can't book more than one slot");
        }else{
            userWithId.addBookedTrip(tripWithId);
            tripWithId.bookSlot();

            String travelerMailDto = new JSONObject(new MailDto(userWithId.getEmail(), travelerEmailSubject, travelerEmailBody)).toString();
            String travelAgencyMailDto = new JSONObject(new MailDto(travelAgencyWithTrip.getEmail(), travelAgencyEmailSubject, travelAgencyEmailBOdy)).toString();

            rabbitTemplate.convertAndSend(mailQueue.getName(), travelerMailDto);
            rabbitTemplate.convertAndSend(mailQueue.getName(), travelAgencyMailDto);

            travelerRepository.save(userWithId);
            tripRepository.save(tripWithId);
        }
    }
}
