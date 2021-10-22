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
    private String fiscalName;
    private Long cuit;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Manager manager;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Trip> trips;
}
