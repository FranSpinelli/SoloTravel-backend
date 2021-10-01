package ar.edu.unq.solotravel.backend.api.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "travelAgencies")
@PrimaryKeyJoinColumn(name = "ID")
@Getter
@Setter
public class TravelAgency extends User{

    private String password;
}
