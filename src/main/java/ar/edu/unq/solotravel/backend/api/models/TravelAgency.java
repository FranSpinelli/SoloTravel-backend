package ar.edu.unq.solotravel.backend.api.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "travelAgencies")
@PrimaryKeyJoinColumn(name = "ID")
@Getter
@Setter
public class TravelAgency extends User{

    private String password;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Trip> trips;
}
