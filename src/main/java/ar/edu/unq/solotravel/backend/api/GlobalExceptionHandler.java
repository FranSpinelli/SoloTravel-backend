package ar.edu.unq.solotravel.backend.api;

import ar.edu.unq.solotravel.backend.api.dtos.ErrorResponseDto;
import ar.edu.unq.solotravel.backend.api.exceptions.LogInException;
import ar.edu.unq.solotravel.backend.api.exceptions.NoSuchElementException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity handleJWTVerificationException(JWTVerificationException jwtVerificationException){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDto(jwtVerificationException.getMessage()));
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity handleJWTDecodeException(JWTDecodeException jwtDecodeException){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDto(jwtDecodeException.getMessage()));
    }


}
