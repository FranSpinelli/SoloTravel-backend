package ar.edu.unq.solotravel.backend.api;

import ar.edu.unq.solotravel.backend.api.dtos.ErrorResponseDto;
import ar.edu.unq.solotravel.backend.api.exceptions.ExpiredSessionException;
import ar.edu.unq.solotravel.backend.api.exceptions.InvalidJwtException;
import ar.edu.unq.solotravel.backend.api.exceptions.LogInException;
import ar.edu.unq.solotravel.backend.api.exceptions.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity handleNoSuchElementException(NoSuchElementException noSuchElementException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(noSuchElementException.getMessage()));
    }

    @ExceptionHandler(LogInException.class)
    public ResponseEntity handleLogInException(LogInException logInException){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDto(logInException.getMessage()));
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity handleInvalidJwtException(InvalidJwtException invalidJwtException){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDto(invalidJwtException.getMessage()));
    }

    @ExceptionHandler(ExpiredSessionException.class)
    public ResponseEntity handleExpiredSessionException(ExpiredSessionException expiredSessionException){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDto(expiredSessionException.getMessage()));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity handleMissingRequestHeaderException(MissingRequestHeaderException missingRequestHeaderException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto(missingRequestHeaderException.getMessage()));
    }
}
