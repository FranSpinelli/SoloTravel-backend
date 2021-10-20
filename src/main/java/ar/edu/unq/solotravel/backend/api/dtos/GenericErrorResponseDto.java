package ar.edu.unq.solotravel.backend.api.dtos;

public class GenericErrorResponseDto extends ErrorResponseDto<String>{

    public GenericErrorResponseDto(String message) {
        super(message);
    }
}
