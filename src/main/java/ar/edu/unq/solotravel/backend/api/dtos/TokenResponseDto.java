package ar.edu.unq.solotravel.backend.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenResponseDto {
    private String token;
    private String agencyName;
    private Integer agencyId;
}
