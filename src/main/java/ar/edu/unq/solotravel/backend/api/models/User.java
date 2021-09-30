package ar.edu.unq.solotravel.backend.api.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String googleId;
    private String name;
    private String email;
    private String picture;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Trip> favorites;

    public void addFavorite(Trip trip){
        if(!favorites.contains(trip)){
            favorites.add(trip);
        }
    }

    public void removeFavorite(Trip trip){
        favorites.remove(trip);
    }
}
