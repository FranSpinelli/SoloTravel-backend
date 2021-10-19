package ar.edu.unq.solotravel.backend.api.exceptions;

public class InvalidJwtException extends RuntimeException {
    public InvalidJwtException() {
        super("You don't have permission to make this request");
    }
}
