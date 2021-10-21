package ar.edu.unq.solotravel.backend.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public abstract class ErrorResponseDto<T> {

    private T message;

    public ErrorResponseDto(T message){
        this.message = message;
    }
}
