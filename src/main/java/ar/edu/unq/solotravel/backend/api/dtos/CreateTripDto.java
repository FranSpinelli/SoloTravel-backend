package ar.edu.unq.solotravel.backend.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateTripDto {
    private String name;
    private String destination;
    private String image;
    private String description;
    private double price;
    private Date startDate;
    private Date endDate;
}
