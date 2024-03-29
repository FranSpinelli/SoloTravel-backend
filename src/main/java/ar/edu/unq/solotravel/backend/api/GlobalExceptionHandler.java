package ar.edu.unq.solotravel.backend.api;

import ar.edu.unq.solotravel.backend.api.dtos.GenericErrorResponseDto;
import ar.edu.unq.solotravel.backend.api.dtos.ValidationErrorResponseDTO;
import ar.edu.unq.solotravel.backend.api.exceptions.*;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity handleNoSuchElementException(NoSuchElementException noSuchElementException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericErrorResponseDto(noSuchElementException.getMessage()));
    }

    @ExceptionHandler(LogInException.class)
    public ResponseEntity handleLogInException(LogInException logInException){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericErrorResponseDto(logInException.getMessage()));
    }

    @ExceptionHandler(RegisterException.class)
    public ResponseEntity handleRegisterException(RegisterException registerException){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericErrorResponseDto(registerException.getMessage()));
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity handleInvalidJwtException(InvalidJwtException invalidJwtException){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericErrorResponseDto(invalidJwtException.getMessage()));
    }

    @ExceptionHandler(ExpiredSessionException.class)
    public ResponseEntity handleExpiredSessionException(ExpiredSessionException expiredSessionException){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericErrorResponseDto(expiredSessionException.getMessage()));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity handleMissingRequestHeaderException(MissingRequestHeaderException missingRequestHeaderException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericErrorResponseDto(missingRequestHeaderException.getMessage()));
    }

    @ExceptionHandler(InvalidImageException.class)
    public ResponseEntity handleInvalidImageException(InvalidImageException invalidImageException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericErrorResponseDto(invalidImageException.getMessage()));
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity handleFileSizeLimitExceededException(FileSizeLimitExceededException fileSizeLimitExceededException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericErrorResponseDto(fileSizeLimitExceededException.getMessage()));
    }

    @ExceptionHandler(InvalidActionException.class)
    public ResponseEntity handleInvalidActionException(InvalidActionException invalidActionException){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericErrorResponseDto(invalidActionException.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){

        Map<String, String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(400).body(new ValidationErrorResponseDTO(errors));
    }
}
