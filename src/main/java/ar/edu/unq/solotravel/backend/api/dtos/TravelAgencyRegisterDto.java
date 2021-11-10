package ar.edu.unq.solotravel.backend.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Valid
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TravelAgencyRegisterDto {

    @NotNull(message = "name field can't be null in the body json")
    @NotEmpty(message = "name field can't be empty in the body json")
    @Length(min = 3, max = 20, message = "name field should has between 3 and 20 characters length")
    private String name;
    @Email(message="Please provide a valid email address")
    @Pattern(regexp=".+@.+\\..+", message="Please provide a valid email address")
    @NotNull(message = "email field can't be null in the body json")
    @NotEmpty(message = "email field can't be empty in the body json")
    @Length(min = 3, max = 40, message = "email field should has between 3 and 40 characters length")
    private String email;
    @NotNull(message = "password field can't be null in the body json")
    @NotEmpty(message = "password field can't be empty in the body json")
    @Length(min = 3, max = 20, message = "password field should has between 3 and 20 characters length")
    private String password;
    @NotNull(message = "fiscal name field can't be null in the body json")
    @NotEmpty(message = "fiscal name field can't be empty in the body json")
    @Length(min = 3, max = 20, message = "fiscal name field should has between 3 and 20 characters length")
    private String fiscalName;
    @NotNull(message = "cuit field can't be null in the body json")
    @Min(value = 11111111111L, message = "Please provide a valid cuit number")
    @Max(value = 99999999999L, message = "Please provide a valid cuit number")
    private Long cuit;

    @NotNull(message = "location province field can't be null in the body json")
    @NotEmpty(message = "location province field can't be empty in the body json")
    @Length(min = 3, max = 20, message = "location province field should has between 3 and 20 characters length")
    private String locationProvince;
    @NotNull(message = "location city field can't be null in the body json")
    @NotEmpty(message = "location city field can't be empty in the body json")
    @Length(min = 3, max = 20, message = "location city field should has between 3 and 20 characters length")
    private String locationCity;
    @NotNull(message = "location street field can't be null in the body json")
    @NotEmpty(message = "location street field can't be empty in the body json")
    @Length(min = 3, max = 20, message = "location street field should has between 3 and 20 characters length")
    private String locationStreet;
    @NotNull(message = "location number field can't be null in the body json")
    @Min(value = 1, message = "locantion number can't be cero or negative")
    private Integer locationNumber;

    @NotNull(message = "manager first name field can't be null in the body json")
    @NotEmpty(message = "manager first name field can't be empty in the body json")
    @Length(min = 3, max = 20, message = "manager first name field should has between 3 and 20 characters length")
    private String managerFirstName;
    @NotNull(message = "manager surname field can't be null in the body json")
    @NotEmpty(message = "manager surname field can't be empty in the body json")
    @Length(min = 3, max = 20, message = "manager first name field should has between 3 and 20 characters length")
    private String managerSurname;
    @NotNull(message = "manager DNI field can't be null in the body json")
    @NotEmpty(message = "manager DNI field can't be empty in the body json")
    @Length(max = 8, message = "manager DNI field should has between 3 and 20 characters length")
    private String managerDni;
    @NotNull(message = "manager cuit field can't be null in the body json")
    @Min(value = 11111111111L, message = "Please provide a valid cuit number")
    @Max(value = 99999999999L, message = "Please provide a valid cuit number")
    private Long managerCuit;
}
