package ar.edu.unq.solotravel.backend.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.index.qual.LessThan;

import javax.validation.constraints.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateTripDto {
    @NotNull(message = "name field can't be null")
    @NotEmpty(message = "name field can't be empty")
    private String name;
    @NotNull(message = "destination field can't be null")
    @NotEmpty(message = "destination field can't be empty")
    private String destination;
    @NotNull(message = "image field can't be null")
    @NotEmpty(message = "image field can't be empty")
    private String image;
    @NotNull(message = "description field can't be null")
    @NotEmpty(message = "description field can't be empty")
    private String description;
    @NotNull(message = "price field can't be null")
    @Min(value = 1, message = "Please provide a valid price")
    @Max(value = 999999, message = "Please provide a valid price")
    private Double price;
    @NotNull(message = "date field can't be null")
    @FutureOrPresent(message = "date can't be a past date")
    private LocalDate startDate;
    @NotNull(message = "date field can't be null")
    @FutureOrPresent(message = "date can't be a past date")
    private LocalDate endDate;
    @NotNull(message = "slots field can't be null")
    @Min(value = 1, message = "Please provide a valid total slots number")
    @Max(value = 99, message = "Please provide a valid total slots number")
    private Integer totalSlots;
}
