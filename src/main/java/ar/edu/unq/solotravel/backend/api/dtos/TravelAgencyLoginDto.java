package ar.edu.unq.solotravel.backend.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Valid
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TravelAgencyLoginDto {
    @Email(message="Please provide a valid email address")
    @Pattern(regexp=".+@.+\\..+", message="Please provide a valid email address")
    @NotNull(message = "email field can't be null in the body json")
    @NotEmpty(message = "email field can't be empty in the body json")
    private String email;
    @NotNull(message = "password field can't be null in the body json")
    @NotEmpty(message = "password field can't be empty in the body json")
    private String password;
}
