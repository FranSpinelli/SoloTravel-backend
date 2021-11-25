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
public class SearchTripParamsDto {
    private String destination;
    private LocalDate date;
}
