package ar.edu.unq.solotravel.backend.api.models;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
public class User {

    @Id
    private Integer id;
    private String name;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Trip> favourites;
}
