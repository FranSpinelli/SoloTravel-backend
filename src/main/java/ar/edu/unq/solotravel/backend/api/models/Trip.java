package ar.edu.unq.solotravel.backend.api.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "trips")
@Getter
@Setter
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String destination;
    private String image;
    @Column(length = 100000)
    private String description;
    private Double price;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private TripCategory category;
    private Integer TotalSlots;
    private Integer AvailableSlots;

    public Integer getDuration(){
        return Math.toIntExact(ChronoUnit.DAYS.between(startDate, endDate));
    }
}
