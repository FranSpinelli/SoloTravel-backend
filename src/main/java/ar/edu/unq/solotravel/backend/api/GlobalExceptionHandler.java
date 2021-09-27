package ar.edu.unq.solotravel.backend.api;

import ar.edu.unq.solotravel.backend.api.dtos.ErrorResponseDto;
import ar.edu.unq.solotravel.backend.api.exceptions.NoSuchElementException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity handleWrongApiKeyException(NoSuchElementException noSuchElementException){
        return ResponseEntity.status(404).body(new ErrorResponseDto(noSuchElementException.getMessage()));
    }
}
