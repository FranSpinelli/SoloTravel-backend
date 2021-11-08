package ar.edu.unq.solotravel.backend.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateTripDto {
    private Integer id;
    private String name;
    private String destination;
    private String image;
    private String description;
    private Double price;
    private LocalDate startDate;
    private LocalDate endDate;
}
