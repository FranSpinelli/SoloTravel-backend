package ar.edu.unq.solotravel.backend.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TripDto {

    private Integer id;
    private String name;
    private String destination;
    private String image;
    private String description;
    private double price;
    private Integer duration;
}
