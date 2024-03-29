package ar.edu.unq.solotravel.backend.api.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "travelers")
@PrimaryKeyJoinColumn(name = "ID")
@Getter
@Setter
public class Traveler extends User{

    private String googleId;
    private String picture;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Trip> favorites;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Trip> bookedTrips;

    public void addFavorite(Trip trip){
        if(!favorites.contains(trip)){
            favorites.add(trip);
        }
    }

    public void removeFavorite(Trip trip){
        favorites.remove(trip);
    }

    public void addBookedTrip(Trip trip){
        if(!bookedTrips.contains(trip)){
            bookedTrips.add(trip);
        }
    }

    public void removeBookedTrip(Trip trip){
        favorites.remove(trip);
    }

    public boolean hasBookedTrip(Trip trip) {
        return bookedTrips.contains(trip);
    }
}
